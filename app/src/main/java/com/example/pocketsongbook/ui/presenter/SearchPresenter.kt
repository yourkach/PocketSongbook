package com.example.pocketsongbook.ui.presenter

import com.example.pocketsongbook.R
import com.example.pocketsongbook.domain.FavouriteSongsDao
import com.example.pocketsongbook.domain.SongsReposFacade
import com.example.pocketsongbook.domain.model.Song
import com.example.pocketsongbook.domain.model.SongSearchItem
import com.example.pocketsongbook.ui.view.SearchSongView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import moxy.InjectViewState
import moxy.MvpPresenter
import javax.inject.Inject

@InjectViewState
class SearchPresenter @Inject constructor(
    private val songsReposFacade: SongsReposFacade,
    private val favouriteSongsDao: FavouriteSongsDao
) : MvpPresenter<SearchSongView>() {


    private var searchQuery: String = ""
    private val searchItems = mutableListOf<SongSearchItem>()
    private var isDownloading: Boolean = false

    fun getSpinnerItems(): List<String> = songsReposFacade.getWebsiteNames()

    fun onQueryTextSubmit(query: String?): Boolean {
        return if (query != null) {
            viewState.clearToolbarFocus()
            performSearch(query)
            true
        } else {
            viewState.showToast(R.string.toast_empty_request)
            false
        }
    }

    private fun performSearch(query: String) {
        CoroutineScope(Dispatchers.Main).launch {
            searchItems.clear()
            searchQuery = query
            viewState.showLoadingPanel(true)
            val searchResult = withContext(Dispatchers.IO) {
                val result = songsReposFacade.getSearchResults(
                    searchQuery
                )
                result?.forEach {
                    it.isFavourite = favouriteSongsDao.findByUrl(it.link).isNotEmpty()
                }
                return@withContext result
            }
            viewState.showLoadingPanel(false)
            if (searchResult == null) {
                viewState.showToast(R.string.toast_error_connection)
            } else {
                if (searchResult.isNotEmpty()) {
                    searchResult.forEach { item -> searchItems.add(item) }
                    viewState.updateRecyclerItems(searchItems)
                } else {
                    viewState.showToast(R.string.toast_no_results)
                }
            }
        }
    }

    fun onSpinnerItemSelected(pos: Int) {
        if (songsReposFacade.switchToRepo(pos)) {
            if (searchQuery != "") {
                performSearch(searchQuery)
                viewState.updateRecyclerItems(listOf())
            }
        }
    }

    fun onSongClicked(pos: Int) {
        if (!isDownloading) {
            CoroutineScope(Dispatchers.Main).launch {
                isDownloading = true
                viewState.showLoadingPanel(true)
                val song = withContext(Dispatchers.IO) {
                    return@withContext favouriteSongsDao.findByUrl(searchItems[pos].link)
                        .firstOrNull()?.let { Song(it) }
                        ?: songsReposFacade.getSong(searchItems[pos])
                }
                viewState.showLoadingPanel(false)
                isDownloading = false
                if (song != null) {
                    viewState.startSongViewActivity(song)
                } else {
                    viewState.showToast(R.string.toast_download_fail)
                }
            }
        }
    }

    fun onFavouritesClicked() {
        viewState.startFavouritesActivity()
    }
}
