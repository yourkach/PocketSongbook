package com.example.pocketsongbook.feature.search.mvi.event_reducers

import com.example.pocketsongbook.common.mvi_core.EventReducer
import com.example.pocketsongbook.feature.search.SearchItemsState
import com.example.pocketsongbook.feature.search.SearchViewState
import com.example.pocketsongbook.feature.search.mvi.SearchScreenEvent
import javax.inject.Inject

class WebsiteChangeReducer @Inject constructor() :
    EventReducer<SearchScreenEvent.WebsiteChanged, SearchViewState> {

    override fun reduceBy(
        currentState: SearchViewState,
        event: SearchScreenEvent.WebsiteChanged
    ): SearchViewState {
        return currentState.copy(
            selectedWebsite = event.website,
            searchItemsState = SearchItemsState.Empty
        )
    }

}

