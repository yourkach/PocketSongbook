package com.example.pocketsongbook.feature.search

import com.example.pocketsongbook.R
import com.example.pocketsongbook.data.models.Song
import com.example.pocketsongbook.data.models.SongSearchItem
import com.example.pocketsongbook.common.BasePresenter
import com.example.pocketsongbook.common.BaseView
import com.example.pocketsongbook.common.extensions.setAndCancelJob
import com.example.pocketsongbook.data.favourites.FavouriteSongsRepo
import com.example.pocketsongbook.data.network.WebsitesManager
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import moxy.InjectViewState
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
    fun setWebsites(websiteNames: List<String>)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setWebsiteSelected(websiteName: String)

}

@InjectViewState
@Singleton
class SearchPresenter @Inject constructor(
//    private val getWebsiteNamesUseCase: GetWebsiteNamesUseCase,
//    private val switchToWebSiteUseCase: SwitchToWebSiteUseCase,
//    private val getSearchResultsUseCase: GetSearchResultsUseCase,
//    private val getSongUseCase: GetSongUseCase
    private val websitesManager: WebsitesManager,
    private val favouriteSongsRepo: FavouriteSongsRepo
) : BasePresenter<SearchSongView>() {

    private var lastSearchQuery: String? = null

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        setUpWebsiteNames()
        startCollectQuery()
    }

    private val searchQueryChannel = Channel<String>()

    fun onQueryTextChange(newText: String) {
        launch {
            searchQueryChannel.send(newText)
        }
    }

    private fun startCollectQuery() {
        launch {
            searchQueryChannel.consumeAsFlow()
                .distinctUntilChanged()
                .debounce(700)
                .collect { query ->
                    performSearch(query)
                }
        }
    }

    private fun setUpWebsiteNames() {
        launch {
            val websiteNames = websitesManager.getWebsiteNames()
            viewState.setWebsites(websiteNames)
            viewState.setWebsiteSelected(websitesManager.selectedWebsiteName)
        }
    }


    var searchJob: Job? by setAndCancelJob()
    private fun performSearch(query: String) {
        searchJob = launch {
            try {
                viewState.showLoading()
                // TODO: 05.11.20 вынести в юзкейс
                val searchResult = withContext(Dispatchers.IO) {
                    websitesManager.getSearchResults(query)
                        .map { it.copy(isFavourite = favouriteSongsRepo.containsSong(it.url)) }
                }
                viewState.updateRecyclerItems(searchResult)
            } finally {
                viewState.hideLoading()
            }
        }
    }

    fun onWebsiteItemSelected(websiteName: String) {
        launch {
            val switchSuccessful = websitesManager.selectByName(websiteName)
            if (switchSuccessful) {
                viewState.setWebsiteSelected(websiteName)
                if (!lastSearchQuery.isNullOrEmpty()) performSearch(lastSearchQuery!!)
            }
        }
    }

    private var loadSongJob: Job? = null
    fun onSongClicked(searchItem: SongSearchItem) {
        if (loadSongJob?.isActive != true) {
            loadSongJob = launch {
                try {
                    viewState.showLoading()
                    val song = withContext(Dispatchers.IO) {
                        websitesManager.getSong(searchItem)
                    }
                    if (song != null) {
                        viewState.navigateToSongView(song)
                    } else {
                        viewState.showMessage(R.string.toast_download_fail)
                    }
                } finally {
                    viewState.hideLoading()
                }
            }
        }
    }

    override fun onFailure(e: Throwable) {
        super.onFailure(e)
        viewState.showMessage(R.string.error_connection)
    }

    fun onFavouritesClicked() {
        viewState.navigateToFavourites()
    }
}
