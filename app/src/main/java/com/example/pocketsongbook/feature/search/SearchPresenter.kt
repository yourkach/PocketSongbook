package com.example.pocketsongbook.feature.search

import com.example.pocketsongbook.R
import com.example.pocketsongbook.data.models.Song
import com.example.pocketsongbook.data.models.SongSearchItem
import com.example.pocketsongbook.common.BasePresenter
import com.example.pocketsongbook.common.BaseView
import com.example.pocketsongbook.common.extensions.setAndCancelJob
import com.example.pocketsongbook.data.network.WebsiteParsersManager
import com.example.pocketsongbook.feature.search.usecase.GetSearchResultsUseCase
import com.example.pocketsongbook.feature.search.usecase.LoadSongUseCase
import com.example.pocketsongbook.feature.search.usecase.GetWebsiteNamesUseCase
import com.example.pocketsongbook.feature.search.usecase.SelectWebsiteByNameUseCase
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
    fun toSongScreen(song: Song)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun toFavouritesScreen()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setWebsites(websiteNames: List<String>)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setWebsiteSelected(websiteName: String)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun setSearchItems(newItems: List<SongSearchItem>)

}

@InjectViewState
@Singleton
class SearchPresenter @Inject constructor(
    private val getWebsiteNamesUseCase: GetWebsiteNamesUseCase,
    private val selectWebsiteByNameUseCase: SelectWebsiteByNameUseCase,
    private val getSearchResultsUseCase: GetSearchResultsUseCase,
    private val loadSongUseCase: LoadSongUseCase
) : BasePresenter<SearchSongView>() {

    private var lastSearchQuery: String? = null

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        setWebsiteNames()
        startCollectingQuery()
    }

    private val searchQueryChannel = Channel<String>()

    fun onQueryTextChange(newText: String) {
        launch {
            searchQueryChannel.send(newText)
        }
    }

    private fun startCollectingQuery() {
        launch {
            searchQueryChannel.consumeAsFlow()
                .distinctUntilChanged()
                .debounce(700)
                .collect { query ->
                    startSearchJob(query)
                }
        }
    }

    private fun setWebsiteNames() {
        launch {
            getWebsiteNamesUseCase().let { (websiteNames, selectedWebsite) ->
                viewState.setWebsites(websiteNames)
                viewState.setWebsiteSelected(selectedWebsite)
            }
        }
    }


    var searchJob: Job? by setAndCancelJob()
    private fun startSearchJob(query: String) {
        searchJob = launch {
            withLoading {
                lastSearchQuery = query
                val searchResult = withContext(Dispatchers.IO) {
                    getSearchResultsUseCase(query)
                }
                viewState.setSearchItems(searchResult)
            }
        }
    }

    fun onWebsiteItemSelected(websiteName: String) {
        launch {
            selectWebsiteByNameUseCase(websiteName).let { switchSuccessful ->
                if (switchSuccessful) {
                    viewState.setWebsiteSelected(websiteName)
                    if (!lastSearchQuery.isNullOrEmpty()) startSearchJob(lastSearchQuery!!)
                }
            }
        }
    }

    private var loadSongJob: Job? = null
    fun onSongClicked(searchItem: SongSearchItem) {
        if (loadSongJob?.isActive != true) {
            loadSongJob = launch {
                withLoading {
                    val song = withContext(Dispatchers.IO) {
                        loadSongUseCase(searchItem)
                    }
                    if (song != null) {
                        viewState.toSongScreen(song)
                    } else {
                        viewState.showMessage(R.string.error_failed_to_load_song)
                    }
                }
            }
        }
    }

    override fun onFailure(e: Throwable) {
        super.onFailure(e)
        viewState.showMessage(R.string.error_no_connection)
    }

    fun onFavouritesClicked() {
        viewState.toFavouritesScreen()
    }
}
