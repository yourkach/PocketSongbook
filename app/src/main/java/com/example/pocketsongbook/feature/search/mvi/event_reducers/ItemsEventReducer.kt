package com.example.pocketsongbook.feature.search.mvi.event_reducers

import com.example.pocketsongbook.common.mvi_core.EventReducer
import com.example.pocketsongbook.feature.search.SearchItemsState
import com.example.pocketsongbook.feature.search.SearchViewState
import com.example.pocketsongbook.feature.search.mvi.SearchScreenEvent
import javax.inject.Inject

class ItemsEventReducer @Inject constructor() :
    EventReducer<SearchScreenEvent.SearchItemsEvent, SearchViewState> {

    override fun reduceBy(
        currentState: SearchViewState,
        event: SearchScreenEvent.SearchItemsEvent
    ): SearchViewState {
        val newItemsState = event.toItemsState()
        return currentState.updateSearchItemsState(itemsState = newItemsState)
            .withHiddenSuggestions()
            .withClearQueryFocus()
    }

    private fun SearchScreenEvent.SearchItemsEvent.toItemsState(): SearchItemsState {
        return when (this) {
            is SearchScreenEvent.SearchItemsEvent.LoadingStarted -> {
                SearchItemsState.SearchResult.Loading(query, website)
            }
            is SearchScreenEvent.SearchItemsEvent.Loaded -> {
                when (items.isNotEmpty()) {
                    true -> SearchItemsState.SearchResult.Loaded(items, query, website)
                    false -> SearchItemsState.SearchResult.NothingFound(query, website)
                }
            }
            is SearchScreenEvent.SearchItemsEvent.Failed -> {
                SearchItemsState.SearchResult.Failed(error, query, website)
            }
        }
    }

    private fun SearchViewState.updateSearchItemsState(
        itemsState: SearchItemsState
    ): SearchViewState = copy(searchItemsState = itemsState)

    private fun SearchViewState.withHiddenSuggestions(): SearchViewState =
        copy(suggestionsState = suggestionsState.copy(isVisible = false))

    private fun SearchViewState.withClearQueryFocus(): SearchViewState =
        copy(searchQueryState = searchQueryState.copy(isFocused = false))

}