package com.example.pocketsongbook.ui.presenter

import com.example.pocketsongbook.R
import com.example.pocketsongbook.domain.FavouriteSongsDao
import com.example.pocketsongbook.domain.SongsApiManager
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
    private val songsApiManager: SongsApiManager,
    private val favouriteSongsDao: FavouriteSongsDao
) : MvpPresenter<SearchSongView>() {


    private var searchQuery: String = ""
    private val searchItems = mutableListOf<SongSearchItem>()
    private var isDownloading: Boolean = false

    fun getSpinnerItems(): List<String> = songsApiManager.getWebsiteNames()

    fun onQueryTextSubmit(query: String?): Boolean {
        return if (query != null) {
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
                val result = songsApiManager.getSearchResults(
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
        if (songsApiManager.switchToWebsite(pos)) {
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
                        ?: songsApiManager.getSong(searchItems[pos])
                }
                viewState.showLoadingPanel(false)
                isDownloading = false
                if (song != null) {
                    viewState.navigateToSongView(song)
                } else {
                    viewState.showToast(R.string.toast_download_fail)
                }
            }
        }
    }

    fun onFavouritesClicked() {
        viewState.navigateToFavourites()
    }
}
