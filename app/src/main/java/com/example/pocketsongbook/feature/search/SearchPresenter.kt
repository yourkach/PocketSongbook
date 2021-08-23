package com.example.pocketsongbook.feature.search

import com.example.pocketsongbook.common.BasePresenter
import com.example.pocketsongbook.common.extensions.setAndCancelJob
import com.example.pocketsongbook.common.mvi_core.StateListener
import com.example.pocketsongbook.data.search.website_parsers.LoadSearchResultsError
import com.example.pocketsongbook.data.search.website_parsers.ParseSongPageError
import com.example.pocketsongbook.domain.event_bus.Event
import com.example.pocketsongbook.domain.event_bus.SubscribeToEventsUseCase
import com.example.pocketsongbook.domain.models.FoundSongModel
import com.ybond.core.entities.SongsWebsite
import com.example.pocketsongbook.feature.search.mvi.SearchScreenEvent
import com.example.pocketsongbook.feature.search.mvi.SearchScreenReducer
import com.example.pocketsongbook.feature.search.usecase.DeleteQuerySuggestionUseCase
import com.example.pocketsongbook.feature.search.usecase.GetQuerySuggestionsUseCase
import com.example.pocketsongbook.feature.search.usecase.GetSearchResultsUseCase
import com.example.pocketsongbook.feature.search.usecase.LoadSongModelUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import moxy.InjectViewState
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject


@InjectViewState
class SearchPresenter @Inject constructor(
    private val getSearchResultsUseCase: GetSearchResultsUseCase,
    private val loadSongModelUseCase: LoadSongModelUseCase,
    private val subscribeToEventsUseCase: SubscribeToEventsUseCase,
    private val getQuerySuggestionsUseCase: GetQuerySuggestionsUseCase,
    private val deleteQuerySuggestionUseCase: DeleteQuerySuggestionUseCase,
    private val screenStateReducer: SearchScreenReducer
) : BasePresenter<SearchSongView>(), StateListener<SearchViewState> {

    private var currentViewState: SearchViewState = SearchScreenReducer.INITIAL_STATE
        set(value) {
            field = value
            viewState.renderViewState(value)
        }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        subscribeToEvents()
        screenStateReducer.subscribe(this)
        getInitialSuggestions()
    }

    private fun getInitialSuggestions() {
        launch {
            val suggestions = getQuerySuggestionsUseCase(query = "")
            screenStateReducer.obtainEvent(
                SearchScreenEvent.SuggestionsChanged(
                    targetQuery = "",
                    suggestionsList = suggestions
                )
            )
        }
    }

    override fun onNewState(state: SearchViewState) {
        currentViewState = state
    }

    private fun subscribeToEvents() {
        launch {
            subscribeToEventsUseCase { event ->
                if (event is Event.FavoritesChange) {
                    screenStateReducer.obtainEvent(
                        SearchScreenEvent.FavoriteChanged(
                            url = event.url,
                            isFavorite = event.isAdded
                        )
                    )
                }
            }
        }
    }

    fun onSearchFieldFocusChanged(isFocused: Boolean) {
        screenStateReducer.obtainEvent(SearchScreenEvent.QueryEvent.QueryFocusChanged(isFocused))
    }

    private var lastQueryChange: String? = null // to get distinct values
    private var reloadSuggestionsJob by setAndCancelJob()
    fun onQueryTextChange(query: String, isQueryFocused: Boolean) {
        if (lastQueryChange == query) return
        lastQueryChange = query
        reloadSuggestionsJob = launch {
            screenStateReducer.obtainEvent(
                SearchScreenEvent.QueryEvent.QueryChanged(
                    queryText = query,
                    isFocused = isQueryFocused
                )
            )
            reloadSuggestions(query)
        }
    }

    fun onQueryTextSubmit(query: String) {
        if (currentViewState.wasLastSearchForQuery(query)) return
        searchByQuery(query)
    }

    private fun SearchViewState.wasLastSearchForQuery(query: String): Boolean {
        val currentItemsState = searchItemsState as? SearchItemsState.SearchResult ?: return false
        return currentItemsState.query == query
    }

    private var searchJob: Job? by setAndCancelJob()
    private fun searchByQuery(query: String) {
        if (query.isBlank()) return
        searchJob = launch {
            val website = currentViewState.selectedWebsite
            screenStateReducer.obtainEvent(
                SearchScreenEvent.SearchItemsEvent.LoadingStarted(query, website)
            )
            try {
                val items = getSearchResultsUseCase(website, query.trim())
                screenStateReducer.obtainEvent(
                    SearchScreenEvent.SearchItemsEvent.Loaded(query, website, items)
                )
            } catch (e: LoadSearchResultsError) {
                screenStateReducer.obtainEvent(
                    SearchScreenEvent.SearchItemsEvent.Failed(query, website, e)
                )
            }
        }
    }

    fun onWebsiteSelected(website: SongsWebsite) {
        if (currentViewState.selectedWebsite == website) return
        screenStateReducer.obtainEvent(SearchScreenEvent.WebsiteChanged(website))
        searchByQuery(currentViewState.searchQueryState.queryText)
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
        when (e) {
            is LoadSearchResultsError.ParsingError -> viewState.showSearchFailedError()
            is ParseSongPageError -> viewState.showFailedToLoadSongError()
            is LoadSearchResultsError.ConnectionError,
            is SocketTimeoutException,
            is UnknownHostException -> viewState.showInternetConnectionError()
        }
    }

    fun onSuggestionClick(suggestion: String) {
        screenStateReducer.obtainEvent(
            SearchScreenEvent.QueryEvent.QueryChanged(queryText = suggestion, isFocused = false)
        )
        onQueryTextSubmit(suggestion)
    }

    fun onSuggestionDeleteClick(suggestion: String) {
        reloadSuggestionsJob = launch {
            deleteQuerySuggestionUseCase(suggestionText = suggestion)
            reloadSuggestions(currentViewState.searchQueryState.queryText)
        }
    }

    private suspend fun reloadSuggestions(query: String) {
        val suggestions = getQuerySuggestionsUseCase(query)
        screenStateReducer.obtainEvent(
            SearchScreenEvent.SuggestionsChanged(
                targetQuery = query,
                suggestionsList = suggestions
            )
        )
    }

}
