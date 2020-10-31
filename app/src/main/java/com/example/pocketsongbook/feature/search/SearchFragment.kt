package com.example.pocketsongbook.feature.search

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pocketsongbook.R
import com.example.pocketsongbook.common.BaseFragment
import com.example.pocketsongbook.data.models.Song
import com.example.pocketsongbook.data.models.SongSearchItem
import com.example.pocketsongbook.feature.favourites.FavouritesFragment
import com.example.pocketsongbook.feature.search.list.SearchAdapter
import com.example.pocketsongbook.feature.search.list.SelectableItemsAdapter
import com.example.pocketsongbook.feature.song.SongFragment
import com.example.pocketsongbook.common.navigation.*
import com.example.pocketsongbook.utils.hideKeyboard
import com.github.terrakok.cicerone.Router
import kotlinx.android.synthetic.main.fragment_search.*
import moxy.ktx.moxyPresenter
import javax.inject.Inject

// TODO: 18.07.20 сделать пагинацию для результатов поиска
// TODO: 18.07.20 добавить поле для отображения количества найденных песен
// TODO: 18.07.20 сделать FavouriteSongsRepository, хранить Url'ы в префсах для быстрой проверки

class SearchFragment : BaseFragment(R.layout.fragment_search),
    SearchSongView {

    @Inject
    lateinit var searchPresenter: SearchPresenter

    @Inject
    lateinit var router: Router

    private val presenter by moxyPresenter { searchPresenter }

    private val searchItemsAdapter by lazy {
        SearchAdapter { item ->
            searchViewMain.hideKeyboard()
            presenter.onSongClicked(item)
        }
    }

    private val websitesAdapter by lazy {
        SelectableItemsAdapter { websiteName ->
            presenter.onWebsiteItemSelected(websiteName)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initSearchView()
        initWebsiteSelector()

        searchOpenFavouritesIv.setOnClickListener {
            presenter.onFavouritesClicked()
        }
    }

    private fun initSearchView() {
        searchViewMain.apply {
            val id = context.resources.getIdentifier("android:id/search_src_text", null, null)
            findViewById<AutoCompleteTextView>(id).setTextColor(requireContext().getColor(R.color.colorPrimaryDark))
            setOnQueryTextListener(
                object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        if (!query.isNullOrEmpty()) {
                            presenter.onQueryTextChange(query)
                            searchViewMain.hideKeyboard()
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

    override fun navigateToSongView(song: Song) {
        router.navigateTo(
            SongFragment.SongArgs(song).toFragment().toScreen(),
            clearContainer = false
        )
    }

    override fun navigateToFavourites() {
        router.navigateTo(FavouritesFragment().toScreen(), clearContainer = false)
    }


    private fun initRecyclerView() {
        searchRv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = searchItemsAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }

    private fun initWebsiteSelector() {
        websiteSelectorContainer.outlineProvider = null
        searchWebsiteSelector.apply {
            adapter = websitesAdapter
        }
    }


    override fun setWebsites(websiteNames: List<String>) {
        websitesAdapter.setWebsiteNames(websiteNames)
    }

    override fun setWebsiteSelected(websiteName: String) {
        websitesAdapter.setSelectedByName(websiteName)
    }

    override fun updateRecyclerItems(newItems: List<SongSearchItem>) {
        nothingFoundStub.isVisible = newItems.isEmpty()
        searchItemsAdapter.apply {
            setList(newItems)
        }
    }

}