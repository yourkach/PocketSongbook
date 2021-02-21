package com.example.pocketsongbook.feature.search

import com.example.pocketsongbook.R
import com.example.pocketsongbook.common.BasePresenter
import com.example.pocketsongbook.common.BaseView
import com.example.pocketsongbook.common.extensions.setAndCancelJob
import com.example.pocketsongbook.data.models.FoundSongModel
import com.example.pocketsongbook.data.models.SongModel
import com.example.pocketsongbook.domain.SongsWebsite
import com.example.pocketsongbook.domain.event_bus.Event
import com.example.pocketsongbook.domain.event_bus.SubscribeToEventsUseCase
import com.example.pocketsongbook.feature.search.usecase.GetSearchResultsUseCase
import com.example.pocketsongbook.feature.search.usecase.LoadSongModelUseCase
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import moxy.InjectViewState
import moxy.presenterScope
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.SkipStrategy
import moxy.viewstate.strategy.StateStrategyType
import javax.inject.Inject


@StateStrategyType(SkipStrategy::class)
interface SearchSongView : BaseView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun toSongScreen(song: SongModel)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun toFavouritesScreen()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setWebsiteSelected(website: SongsWebsite)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun setSearchItems(newItems: List<FoundSongModel>)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showFailedToLoadSongError()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun dismissWebsitesSelector()

    @StateStrategyType(SkipStrategy::class)
    fun showSearchItemsLoading()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showSearchFailedError()

}

@InjectViewState
class SearchPresenter @Inject constructor(
    private val getSearchResultsUseCase: GetSearchResultsUseCase,
    private val loadSongModelUseCase: LoadSongModelUseCase,
    private val subscribeToEventsUseCase: SubscribeToEventsUseCase
) : BasePresenter<SearchSongView>() {

    private var lastSearchQuery: String? = null
    private var selectedWebsite: SongsWebsite = SongsWebsite.AmDm
        set(value) {
            if (field == value) return
            field = value
            viewState.setWebsiteSelected(field)
            lastSearchQuery?.let(::startSearchJob)
        }

    private var loadedItems: List<FoundSongModel> = listOf()
        private set(value) {
            field = value
            viewState.setSearchItems(loadedItems)
        }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        startCollectingQuery()
        subscribeToEvents()
        viewState.setWebsiteSelected(selectedWebsite)
    }

    private fun subscribeToEvents() {
        launch {
            subscribeToEventsUseCase { event ->
                when (event) {
                    is Event.FavoritesChange -> {
                        withContext(Dispatchers.Default) {
                            if (loadedItems.any { it.url == event.url }) {
                                loadedItems.map { songModel ->
                                    if (songModel.url == event.url) {
                                        songModel.copy(isFavourite = event.isAdded)
                                    } else songModel
                                }
                            } else null
                        }?.let { updatedItems ->
                            loadedItems = updatedItems
                        }
                    }
                }
            }
        }
    }

    private val searchQueryFlow = MutableSharedFlow<String>()

    fun onQueryTextChange(newText: String) {
        presenterScope.launch {
            searchQueryFlow.emit(newText)
        }
    }

    private fun startCollectingQuery() {
        launch {
            searchQueryFlow
                .distinctUntilChanged()
                .debounce(700)
                .collect { query ->
                    startSearchJob(query)
                }
        }
    }

    private var searchJob: Job? by setAndCancelJob()
    private fun startSearchJob(query: String) {
        searchJob = launch {
            viewState.showSearchItemsLoading()
            lastSearchQuery = query
            runCatching {
                getSearchResultsUseCase(selectedWebsite, query)
            }.getOrNull()?.let { newItems ->
                loadedItems = newItems
            } ?: let {
                viewState.showSearchFailedError()
            }
        }
    }

    fun onWebsiteSelected(website: SongsWebsite) {
        selectedWebsite = website
        launch {
            delay(100)
            viewState.dismissWebsitesSelector()
        }
    }

    private var loadSongJob: Job? = null
    fun onSongClicked(searchItem: FoundSongModel) {
        if (loadSongJob?.isActive != true) {
            loadSongJob = launch {
                withLoading {
                    loadSongModelUseCase(searchItem)?.let { song ->
                        viewState.toSongScreen(song)
                    } ?: let {
                        viewState.showFailedToLoadSongError()
                    }
                }
            }
        }
    }

    override fun onFailure(e: Throwable) {
        viewState.showMessage(R.string.error_no_connection)
    }

    fun onFavouritesClicked() {
        viewState.toFavouritesScreen()
    }
}
