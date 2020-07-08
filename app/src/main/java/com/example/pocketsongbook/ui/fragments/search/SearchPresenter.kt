package com.example.pocketsongbook.ui.fragments.search

import com.example.pocketsongbook.R
import com.example.pocketsongbook.domain.database.FavouriteSongsDao
import com.example.pocketsongbook.domain.api.SongsApiManager
import com.example.pocketsongbook.domain.models.Song
import com.example.pocketsongbook.domain.models.SongSearchItem
import com.example.pocketsongbook.ui.fragments.BasePresenter
import com.example.pocketsongbook.ui.fragments.search.interactor.GetSearchResultsUseCase
import com.example.pocketsongbook.ui.fragments.search.interactor.GetSongUseCase
import com.example.pocketsongbook.ui.fragments.search.interactor.GetWebsiteNamesUseCase
import com.example.pocketsongbook.ui.fragments.search.interactor.SwitchToWebSiteUseCase
import kotlinx.coroutines.*
import moxy.InjectViewState
import moxy.MvpPresenter
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.SkipStrategy
import moxy.viewstate.strategy.StateStrategyType
import javax.inject.Inject


@StateStrategyType(SkipStrategy::class)
interface SearchSongView : MvpView {

    @StateStrategyType(SkipStrategy::class)
    fun showLoadingPanel(visible: Boolean)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun updateRecyclerItems(newItems: List<SongSearchItem>)

    @StateStrategyType(SkipStrategy::class)
    fun showToast(messageId: Int)

    @StateStrategyType(SkipStrategy::class)
    fun navigateToSongView(song: Song)

    @StateStrategyType(SkipStrategy::class)
    fun navigateToFavourites()

}

@InjectViewState
class SearchPresenter @Inject constructor(
    private val getWebsiteNamesUseCase: GetWebsiteNamesUseCase,
    private val switchToWebSiteUseCase: SwitchToWebSiteUseCase,
    private val getSearchResultsUseCase: GetSearchResultsUseCase,
    private val getSongUseCase: GetSongUseCase
) : BasePresenter<SearchSongView>() {


    private var searchQuery: String = ""
    private val searchItems = mutableListOf<SongSearchItem>()
    private var isDownloading: Boolean = false

    fun getSpinnerItems(): List<String> {
        return runBlocking {
            getWebsiteNamesUseCase.execute(Unit)
        }
    }

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
        doInMainContext {
            searchItems.clear()
            searchQuery = query
            viewState.showLoadingPanel(true)
            val searchResult = getSearchResultsUseCase.execute(query)
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
        doInMainContext {
            val switched = switchToWebSiteUseCase.execute(pos)
            if(switched) performSearch(searchQuery)
        }
    }

    fun onSongClicked(pos: Int) {
        if (!isDownloading) {
            doInMainContext {
                isDownloading = true
                viewState.showLoadingPanel(true)
                val song = getSongUseCase.execute(searchItems[pos])
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
