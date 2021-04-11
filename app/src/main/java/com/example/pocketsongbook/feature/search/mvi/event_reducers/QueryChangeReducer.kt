package com.example.pocketsongbook.feature.search.mvi.event_reducers

import com.example.pocketsongbook.common.mvi_core.EventReducer
import com.example.pocketsongbook.feature.search.SearchQueryState
import com.example.pocketsongbook.feature.search.SearchViewState
import com.example.pocketsongbook.feature.search.mvi.SearchScreenEvent
import javax.inject.Inject

class QueryChangeReducer @Inject constructor() :
    EventReducer<SearchScreenEvent.QueryEvent, SearchViewState> {

    override fun reduceBy(
        currentState: SearchViewState,
        event: SearchScreenEvent.QueryEvent
    ): SearchViewState {
        return when (event) {
            is SearchScreenEvent.QueryEvent.QueryFocusChanged -> {
                currentState.updateQueryState(isFocused = event.isFocused)
                    .updateSuggestionsVisibility(isVisible = event.isFocused)
            }
            is SearchScreenEvent.QueryEvent.QueryChanged -> {
                currentState.updateQueryState(
                    isFocused = event.isFocused,
                    queryText = event.queryText
                )
            }
        }
    }

    private fun SearchViewState.updateQueryState(
        isFocused: Boolean = this.searchQueryState.isFocused,
        queryText: String = this.searchQueryState.queryText
    ): SearchViewState {
        return copy(
            searchQueryState = SearchQueryState(
                isFocused = isFocused,
                queryText = queryText
            )
        )
    }

    private fun SearchViewState.updateSuggestionsVisibility(
        isVisible: Boolean
    ): SearchViewState = copy(suggestionsState = suggestionsState.copy(isVisible = isVisible))

}

