package com.example.pocketsongbook.feature.search

import com.example.pocketsongbook.R
import com.example.pocketsongbook.data.models.Song
import com.example.pocketsongbook.data.models.SongSearchItem
import com.example.pocketsongbook.common.BasePresenter
import com.example.pocketsongbook.common.BaseView
import com.example.pocketsongbook.feature.search.usecase.GetSearchResultsUseCase
import com.example.pocketsongbook.feature.search.usecase.GetSongUseCase
import com.example.pocketsongbook.feature.search.usecase.GetWebsiteNamesUseCase
import com.example.pocketsongbook.feature.search.usecase.SwitchToWebSiteUseCase
import kotlinx.coroutines.*
import moxy.InjectViewState
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.SkipStrategy
import moxy.viewstate.strategy.StateStrategyType
import javax.inject.Inject
import javax.inject.Singleton


@StateStrategyType(SkipStrategy::class)
interface SearchSongView : BaseView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun updateRecyclerItems(newItems: List<SongSearchItem>)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun navigateToSongView(song: Song)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun navigateToFavourites()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setWebsites(websiteNames: List<String>, selectedWebsitePosition: Int)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setWebsiteSelected(selectedWebsitePosition: Int)

}

@InjectViewState
@Singleton
class SearchPresenter @Inject constructor(
    private val getWebsiteNamesUseCase: GetWebsiteNamesUseCase,
    private val switchToWebSiteUseCase: SwitchToWebSiteUseCase,
    private val getSearchResultsUseCase: GetSearchResultsUseCase,
    private val getSongUseCase: GetSongUseCase
) : BasePresenter<SearchSongView>() {

    private var lastSearchQuery: String? = null
    private val searchItems = mutableListOf<SongSearchItem>()
    private var isDownloading: Boolean = false

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        setUpWebsiteNames()
    }


    fun onQueryTextSubmit(query: String): Boolean {
        return if (query.isNotEmpty() && query != lastSearchQuery) {
            lastSearchQuery = query
            performSearch(query)
            true
        } else false
    }

    private fun setUpWebsiteNames() {
        launch {
            val response = withContext(Dispatchers.IO) {
                getWebsiteNamesUseCase(Unit)
            }
            viewState.setWebsites(response.websiteNames, response.selectedWebsitePosition)
        }
    }

    private fun performSearch(query: String) {
        launch {
            searchItems.clear()
            viewState.showLoading()
            val searchResult = withContext(Dispatchers.IO) {
                getSearchResultsUseCase(query)
            }
            viewState.hideLoading()
            if (searchResult == null) {
                viewState.showMessage(R.string.toast_error_connection)
            } else {
                searchResult.forEach { item -> searchItems.add(item) }
                viewState.updateRecyclerItems(searchItems)
            }
        }
    }

    fun onWebsiteItemSelected(itemPosition: Int) {
        launch {
            val switchWasDone = withContext(Dispatchers.IO) {
                switchToWebSiteUseCase(itemPosition)
            }
            if (switchWasDone) {
                viewState.setWebsiteSelected(itemPosition)
                if (!lastSearchQuery.isNullOrEmpty()) performSearch(lastSearchQuery!!)
            }
        }
    }

    fun onSongClicked(pos: Int) {
        if (!isDownloading) {
            launch {
                isDownloading = true
                viewState.showLoading()
                val song = withContext(Dispatchers.IO) {
                    getSongUseCase(searchItems[pos])
                }
                viewState.hideLoading()
                isDownloading = false
                if (song != null) {
                    viewState.navigateToSongView(song)
                } else {
                    viewState.showMessage(R.string.toast_download_fail)
                }
            }
        }
    }

    fun onFavouritesClicked() {
        viewState.navigateToFavourites()
    }
}
