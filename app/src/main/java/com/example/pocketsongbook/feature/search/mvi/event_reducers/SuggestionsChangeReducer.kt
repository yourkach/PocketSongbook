package com.example.pocketsongbook.feature.search.mvi.event_reducers

import com.example.pocketsongbook.common.mvi_core.EventReducer
import com.example.pocketsongbook.feature.search.SearchViewState
import com.example.pocketsongbook.feature.search.SuggestionsState
import com.example.pocketsongbook.feature.search.mvi.SearchScreenEvent
import javax.inject.Inject

class SuggestionsChangeReducer @Inject constructor() :
    EventReducer<SearchScreenEvent.SuggestionsChanged, SearchViewState> {

    override fun reduceBy(
        currentState: SearchViewState,
        event: SearchScreenEvent.SuggestionsChanged
    ): SearchViewState {
        return if (currentState.searchQueryState.queryText != event.targetQuery) {
            currentState.updateSuggestionsState(
                SuggestionsState(
                    isVisible = false,
                    suggestionsList = listOf()
                )
            )
        } else {
            currentState.updateSuggestionsState(
                SuggestionsState(
                    isVisible = currentState.searchQueryState.isFocused,
                    suggestionsList = event.suggestionsList
                )
            )
        }
    }

    private fun SearchViewState.updateSuggestionsState(
        state: SuggestionsState
    ): SearchViewState = copy(suggestionsState = state)

}