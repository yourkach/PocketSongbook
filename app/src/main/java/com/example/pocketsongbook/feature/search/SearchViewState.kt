package com.example.pocketsongbook.feature.search

import com.example.pocketsongbook.data.search.website_parsers.LoadSearchResultsError
import com.example.pocketsongbook.domain.models.FoundSongModel
import com.ybond.core.entities.SongsWebsite

data class SearchViewState(
    val selectedWebsite: SongsWebsite,
    val searchQueryState: SearchQueryState,
    val suggestionsState: SuggestionsState,
    val searchItemsState: SearchItemsState
)

data class SearchQueryState(
    val isFocused: Boolean,
    val queryText: String
)

data class SuggestionsState(
    val isVisible: Boolean,
    val suggestionsList: List<QuerySuggestion>
)

sealed class SearchItemsState {
    object Empty : SearchItemsState()
    sealed class SearchResult : SearchItemsState() {
        abstract val query: String
        abstract val website: SongsWebsite

        class NothingFound(
            override val query: String,
            override val website: SongsWebsite
        ) : SearchResult()

        class Loading(
            override val query: String,
            override val website: SongsWebsite
        ) : SearchResult()

        data class Loaded(
            val items: List<FoundSongModel>,
            override val query: String,
            override val website: SongsWebsite
        ) : SearchResult()

        class Failed(
            val error: LoadSearchResultsError,
            override val query: String,
            override val website: SongsWebsite
        ) : SearchResult()
    }
}

