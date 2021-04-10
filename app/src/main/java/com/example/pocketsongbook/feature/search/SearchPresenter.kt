package com.example.pocketsongbook.feature.search

import com.example.pocketsongbook.common.BasePresenter
import com.example.pocketsongbook.common.BaseView
import com.example.pocketsongbook.common.extensions.setAndCancelJob
import com.example.pocketsongbook.data.search.website_parsers.ParseSearchPageError
import com.example.pocketsongbook.data.search.website_parsers.ParseSongPageError
import com.example.pocketsongbook.domain.event_bus.Event
import com.example.pocketsongbook.domain.event_bus.SubscribeToEventsUseCase
import com.example.pocketsongbook.domain.models.FoundSongModel
import com.example.pocketsongbook.domain.models.SongModel
import com.example.pocketsongbook.domain.search.SongsWebsite
import com.example.pocketsongbook.feature.search.usecase.GetQuerySuggestionsUseCase
import com.example.pocketsongbook.feature.search.usecase.GetSearchResultsUseCase
import com.example.pocketsongbook.feature.search.usecase.LoadSongModelUseCase
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import moxy.InjectViewState
import moxy.viewstate.strategy.*
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject


@StateStrategyType(SkipStrategy::class)
interface SearchSongView : BaseView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun toSongScreen(song: SongModel)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun toFavouritesScreen()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setWebsiteSelected(website: SongsWebsite)

    @StateStrategyType(AddToEndSingleTagStrategy::class, tag = "search_items")
    fun showSearchItemsLoading()

    @StateStrategyType(AddToEndSingleTagStrategy::class, tag = "search_items")
    fun setSearchItems(newItems: List<FoundSongModel>)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setQuerySuggestions(suggestions: List<String>)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showFailedToLoadSongError()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun dismissWebsitesSelector()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showSearchFailedError()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showInternetConnectionError()

}

@InjectViewState
class SearchPresenter @Inject constructor(
    private val getSearchResultsUseCase: GetSearchResultsUseCase,
    private val loadSongModelUseCase: LoadSongModelUseCase,
    private val subscribeToEventsUseCase: SubscribeToEventsUseCase,
    private val getQuerySuggestionsUseCase: GetQuerySuggestionsUseCase
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
        collectQueryChanges()
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

    private val queryChangesFlow = MutableSharedFlow<String>()
    fun onQueryTextChange(newText: String) {
        launch {
            queryChangesFlow.emit(newText)
        }
    }

    fun onQueryTextSubmit(query: String) {
        startSearchJob(query)
    }

    @FlowPreview
    private fun collectQueryChanges() {
        launch {
            queryChangesFlow
                .distinctUntilChanged()
                .debounce(500)
                .collect { query ->
                    loadQuerySuggestions(query)
                }
        }
    }

    private var loadSuggestionsJob by setAndCancelJob()
    private fun loadQuerySuggestions(queryText: String) {
        loadSuggestionsJob = launch {
            getQuerySuggestionsUseCase(queryText).let(viewState::setQuerySuggestions)
        }
    }

    private var searchJob: Job? by setAndCancelJob()
    private fun startSearchJob(query: String) {
        searchJob = launch {
            hideSuggestions()
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

    private fun hideSuggestions() {
        loadSuggestionsJob?.cancel()
        viewState.setQuerySuggestions(listOf())
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
                    loadSongModelUseCase(searchItem).let { song ->
                        viewState.toSongScreen(song)
                    }
                }
            }
        }
    }

    override fun onFailure(e: Throwable) {
        when(e){
            is ParseSearchPageError -> viewState.showSearchFailedError()
            is ParseSongPageError -> viewState.showFailedToLoadSongError()
            is SocketTimeoutException,
            is UnknownHostException -> viewState.showInternetConnectionError()
        }
    }

    fun onFavouritesClicked() {
        viewState.toFavouritesScreen()
    }
}
