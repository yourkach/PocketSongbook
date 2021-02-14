package com.example.pocketsongbook.feature.search

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pocketsongbook.R
import com.example.pocketsongbook.common.BaseFragment
import com.example.pocketsongbook.data.models.SongModel
import com.example.pocketsongbook.data.models.FoundSongModel
import com.example.pocketsongbook.feature.favourites.FavouritesFragment
import com.example.pocketsongbook.feature.search.list.SearchAdapter
import com.example.pocketsongbook.feature.song.SongFragment
import com.example.pocketsongbook.common.navigation.*
import com.example.pocketsongbook.domain.SongsWebsite
import com.example.pocketsongbook.domain.toSongsWebsiteOrNull
import com.example.pocketsongbook.feature.guitar_tuner.TunerFragment
import com.example.pocketsongbook.utils.hideKeyboard
import com.github.terrakok.cicerone.Router
import kotlinx.android.synthetic.main.fragment_search.*
import moxy.ktx.moxyPresenter
import javax.inject.Inject
import javax.inject.Provider

// TODO: 18.07.20 сделать пагинацию для результатов поиска

class SearchFragment : BaseFragment(R.layout.fragment_search),
    SearchSongView {

    @Inject
    lateinit var searchPresenterProvider: Provider<SearchPresenter>

    @Inject
    lateinit var router: Router

    private val presenter by moxyPresenter { searchPresenterProvider.get() }

    private val searchItemsAdapter by lazy {
        SearchAdapter { item ->
            songsSearchView.hideKeyboard()
            presenter.onSongClicked(item)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initSearchView()
        initWebsitesSelector()

        ivOpenFavorites.setOnClickListener {
            presenter.onFavouritesClicked()
        }
        fabOpenTuner.setOnClickListener {
            router.navigateTo(TunerFragment().toScreen())
        }
    }

    private fun initRecyclerView() {
        searchRv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = searchItemsAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
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
            SongFragment.SongArgs(song).toFragment().toScreen()
        )
    }

    override fun toFavouritesScreen() {
        router.navigateTo(FavouritesFragment().toScreen())
    }

    override fun setSearchItems(newItems: List<FoundSongModel>) {
        nothingFoundStub.isVisible = newItems.isEmpty()
        searchItemsAdapter.setList(newItems)
    }

    override fun showFailedToLoadSongError() {
        val message = getString(R.string.error_failed_to_load_song)
        showError(message)
    }

}