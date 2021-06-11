package com.example.pocketsongbook.feature.search.mvi

import com.example.pocketsongbook.common.mvi_core.BaseMviReducer
import com.ybond.core_entities.models.SongsWebsite
import com.example.pocketsongbook.feature.search.SearchItemsState
import com.example.pocketsongbook.feature.search.SearchQueryState
import com.example.pocketsongbook.feature.search.SearchViewState
import com.example.pocketsongbook.feature.search.SuggestionsState
import com.example.pocketsongbook.feature.search.mvi.event_reducers.*
import javax.inject.Inject

class SearchScreenReducer @Inject constructor(
    private val queryEventReducer: QueryChangeReducer,
    private val websiteEventReducer: WebsiteChangeReducer,
    private val itemsEventReducer: ItemsEventReducer,
    private val favoriteEventReducer: FavoriteChangeReducer,
    private val suggestionsReducer: SuggestionsChangeReducer
) : BaseMviReducer<SearchViewState, SearchScreenEvent>(INITIAL_STATE) {

    override fun reduceEvent(
        currentState: SearchViewState,
        event: SearchScreenEvent
    ): SearchViewState {
        return when (event) {
            is SearchScreenEvent.QueryEvent -> queryEventReducer.reduceBy(currentState, event)
            is SearchScreenEvent.WebsiteChanged -> websiteEventReducer.reduceBy(currentState, event)
            is SearchScreenEvent.SearchItemsEvent -> itemsEventReducer.reduceBy(currentState, event)
            is SearchScreenEvent.FavoriteChanged -> favoriteEventReducer.reduceBy(
                currentState,
                event
            )
            is SearchScreenEvent.SuggestionsChanged -> suggestionsReducer.reduceBy(
                currentState,
                event
            )
        }
    }

    companion object {
        val INITIAL_STATE by lazy {
            SearchViewState(
                selectedWebsite = SongsWebsite.AmDm,
                searchQueryState = SearchQueryState(isFocused = false, queryText = ""),
                suggestionsState = SuggestionsState(isVisible = false, suggestionsList = listOf()),
                searchItemsState = SearchItemsState.Empty
            )
        }
    }

}

