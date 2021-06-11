package com.example.pocketsongbook.feature.search.mvi.event_reducers

import com.example.pocketsongbook.common.mvi_core.EventReducer
import com.ybond.core_entities.models.FoundSongModel
import com.example.pocketsongbook.feature.search.SearchItemsState
import com.example.pocketsongbook.feature.search.SearchViewState
import com.example.pocketsongbook.feature.search.mvi.SearchScreenEvent
import javax.inject.Inject

class FavoriteChangeReducer @Inject constructor() :
    EventReducer<SearchScreenEvent.FavoriteChanged, SearchViewState> {

    override fun reduceBy(
        currentState: SearchViewState,
        event: SearchScreenEvent.FavoriteChanged
    ): SearchViewState {
        val loadedItemsState = currentState.getLoadedItemsStateOrNull() ?: return currentState
        val updatedItems = loadedItemsState.items.map { model ->
            if (model.url == event.url) {
                model.copy(isFavourite = model.isFavourite)
            } else model
        }
        return currentState.copy(searchItemsState = loadedItemsState.updateSearchItems(updatedItems))
    }

    private fun SearchViewState.getLoadedItemsStateOrNull(): SearchItemsState.SearchResult.Loaded? {
        return searchItemsState as? SearchItemsState.SearchResult.Loaded
    }

    private fun SearchItemsState.SearchResult.Loaded.updateSearchItems(newItems: List<FoundSongModel>): SearchItemsState.SearchResult.Loaded {
        return copy(items = newItems)
    }

}
