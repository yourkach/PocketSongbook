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
import moxy.viewstate.strategy.SkipStrategy
import moxy.viewstate.strategy.StateStrategyType
import javax.inject.Inject
import javax.inject.Singleton


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

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setSpinnerItems(spinnerItems: List<String>)

}

@InjectViewState
@Singleton
class SearchPresenter @Inject constructor(
    private val getWebsiteNamesUseCase: GetWebsiteNamesUseCase,
    private val switchToWebSiteUseCase: SwitchToWebSiteUseCase,
    private val getSearchResultsUseCase: GetSearchResultsUseCase,
    private val getSongUseCase: GetSongUseCase
) : BasePresenter<SearchSongView>() {


    private var lastSearchQuery: String = ""
    private val searchItems = mutableListOf<SongSearchItem>()
    private var isDownloading: Boolean = false

//    fun getSpinnerItems(): List<String> {
//        return runBlocking {
//            getWebsiteNamesUseCase(Unit)
//        }
//    }


    fun onQueryTextSubmit(query: String?): Boolean {
        return if (query != null && query != lastSearchQuery) {
            lastSearchQuery = query
            performSearch(query)
            true
        } else {
            if (query.isNullOrEmpty()) viewState.showToast(R.string.toast_empty_request)
            false
        }
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        setUpWebsiteNames()
    }

    private fun setUpWebsiteNames(){
        doInMainContext {
            val items = withContext(Dispatchers.IO) {
                getWebsiteNamesUseCase(Unit)
            }
            viewState.setSpinnerItems(items)
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

    fun onSpinnerItemSelected(pos: Int) {
        doInMainContext {
            val switched = withContext(Dispatchers.IO) {
                switchToWebSiteUseCase(pos)
            }
            if (switched) performSearch(lastSearchQuery)
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
