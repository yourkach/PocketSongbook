package com.example.pocketsongbook.ui.fragments.search

import com.example.pocketsongbook.R
import com.example.pocketsongbook.data.models.Song
import com.example.pocketsongbook.data.models.SongSearchItem
import com.example.pocketsongbook.ui.fragments.BasePresenter
import com.example.pocketsongbook.ui.fragments.search.usecase.GetSearchResultsUseCase
import com.example.pocketsongbook.ui.fragments.search.usecase.GetSongUseCase
import com.example.pocketsongbook.ui.fragments.search.usecase.GetWebsiteNamesUseCase
import com.example.pocketsongbook.ui.fragments.search.usecase.SwitchToWebSiteUseCase
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
interface SearchSongView : MvpView {

    @StateStrategyType(SkipStrategy::class)
    fun showLoadingPanel(visible: Boolean)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun updateRecyclerItems(newItems: List<SongSearchItem>)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showToast(messageId: Int)

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

    override fun onDestroy() {
        super.onDestroy()
    }

    fun onQueryTextSubmit(query: String): Boolean {
        return if (query.isNotEmpty() && query != lastSearchQuery) {
            lastSearchQuery = query
            performSearch(query)
            true
        } else false
    }

    private fun setUpWebsiteNames() {
        doInMainContext {
            val response = withContext(Dispatchers.IO) {
                getWebsiteNamesUseCase(Unit)
            }
            viewState.setWebsites(response.websiteNames, response.selectedWebsitePosition)
        }
    }

    private fun performSearch(query: String) {
        doInMainContext {
            searchItems.clear()
            viewState.showLoadingPanel(true)
            val searchResult = withContext(Dispatchers.IO) {
                getSearchResultsUseCase(query)
            }
            viewState.showLoadingPanel(false)
            if (searchResult == null) {
                viewState.showToast(R.string.toast_error_connection)
            } else {
                searchResult.forEach { item -> searchItems.add(item) }
                viewState.updateRecyclerItems(searchItems)
            }
        }
    }

    fun onWebsiteItemSelected(itemPosition: Int) {
        doInMainContext {
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
            doInMainContext {
                isDownloading = true
                viewState.showLoadingPanel(true)
                val song = withContext(Dispatchers.IO) {
                    getSongUseCase(searchItems[pos])
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
