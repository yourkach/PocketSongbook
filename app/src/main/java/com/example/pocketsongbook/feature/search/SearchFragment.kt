package com.example.pocketsongbook.feature.search

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ListPopupWindow
import android.widget.SearchView
import androidx.core.view.isVisible
import com.example.pocketsongbook.R
import com.example.pocketsongbook.common.BaseFragment
import com.example.pocketsongbook.common.navigation.toScreen
import com.example.pocketsongbook.domain.models.FoundSongModel
import com.example.pocketsongbook.domain.models.SongModel
import com.example.pocketsongbook.domain.search.SongsWebsite
import com.example.pocketsongbook.domain.search.toSongsWebsiteOrNull
import com.example.pocketsongbook.feature.favourites.FavouritesFragment
import com.example.pocketsongbook.feature.guitar_tuner.tuner_screen.TunerFragment
import com.example.pocketsongbook.feature.search.list.SongItemsAdapter
import com.example.pocketsongbook.feature.song.SongFragment
import com.example.pocketsongbook.utils.SearchLayoutManager
import com.example.pocketsongbook.utils.hideKeyboard
import kotlinx.android.synthetic.main.fragment_search.*
import moxy.ktx.moxyPresenter
import javax.inject.Inject
import javax.inject.Provider

// TODO: 18.07.20 сделать пагинацию для результатов поиска

class SearchFragment : BaseFragment(R.layout.fragment_search),
    SearchSongView {

    @Inject
    lateinit var searchPresenterProvider: Provider<SearchPresenter>

    private val presenter by moxyPresenter { searchPresenterProvider.get() }

    private val searchItemsAdapter by lazy {
        SongItemsAdapter { item ->
            songsSearchView.hideKeyboard()
            presenter.onSongClicked(item)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initSearchView()
        initWebsitesSelector()

        fabOpenTuner.setOnClickListener {
            router.navigateTo(TunerFragment().toScreen())
        }
    }

    private fun initRecyclerView() {
        searchRv.apply {
            layoutManager = SearchLayoutManager(requireContext())
            adapter = searchItemsAdapter
        }
    }

    override fun setWebsiteSelected(website: SongsWebsite) {
        tvSelectedWebsite.text = website.websiteName
    }

    private val websitesListPopup by lazy { ListPopupWindow(requireContext()) }
    private fun initWebsitesSelector() {
        val websiteNames = SongsWebsite.values().map { it.websiteName }
        websitesListPopup.apply {
            anchorView = tvSelectedWebsite
            ArrayAdapter(
                requireContext(),
                R.layout.item_website,
                websiteNames
            ).let(::setAdapter)
            setOnItemClickListener { _, _, position, _ ->
                websiteNames[position].toSongsWebsiteOrNull()?.let { website ->
                    presenter.onWebsiteSelected(website)
                }
            }
        }
        tvSelectedWebsite.setOnClickListener {
            websitesListPopup.show()
        }
    }

    override fun dismissWebsitesSelector() {
        websitesListPopup.dismiss()
    }

    override fun showSearchItemsLoading() {
        nothingFoundStub.isVisible = false
        searchItemsAdapter.setLoadingItemsList()
        setScrollingEnabled(false)
    }

    private fun setScrollingEnabled(isEnabled:Boolean){
        (searchRv.layoutManager as? SearchLayoutManager)?.isScrollingEnabled = isEnabled
    }

    override fun showSearchFailedError() {
        // TODO: 14.02.21 сделать нормальную ошибку
        val error = getString(R.string.error_no_connection)
        showError(error)
    }

    private fun initSearchView() {
        songsSearchView.apply {
            val id = context.resources.getIdentifier("android:id/search_src_text", null, null)
            findViewById<AutoCompleteTextView>(id).setTextColor(requireContext().getColor(R.color.colorPrimaryDark))
            setOnQueryTextListener(
                object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        if (!query.isNullOrEmpty()) {
                            presenter.onQueryTextChange(query)
                            songsSearchView.hideKeyboard()
                        }
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        if (!newText.isNullOrEmpty()) {
                            presenter.onQueryTextChange(newText)
                        }
                        return true
                    }
                }
            )
        }
    }

    override fun showLoading() {
        super.showLoading()
        nothingFoundStub.isVisible = false
    }

    override fun toSongScreen(song: SongModel) {
        router.navigateTo(
            SongFragment.newInstance(song).toScreen()
        )
    }

    override fun toFavouritesScreen() {
        router.navigateTo(FavouritesFragment().toScreen())
    }

    override fun setSearchItems(newItems: List<FoundSongModel>) {
        val isEmpty = newItems.isEmpty()
        nothingFoundStub.isVisible = isEmpty
        setScrollingEnabled(!isEmpty)
        searchRv.isNestedScrollingEnabled = !isEmpty
        searchItemsAdapter.setLoadedSongs(newItems)
    }

    override fun showFailedToLoadSongError() {
        val message = getString(R.string.error_failed_to_load_song)
        showError(message)
    }

}