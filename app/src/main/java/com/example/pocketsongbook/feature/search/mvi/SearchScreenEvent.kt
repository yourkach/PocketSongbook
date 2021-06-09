package com.example.pocketsongbook.feature.search.mvi

import com.example.pocketsongbook.data.search.website_parsers.LoadSearchResultsError
import com.ybond.core.models.FoundSongModel
import com.ybond.core.models.SongsWebsite
import com.example.pocketsongbook.feature.search.QuerySuggestion

sealed class SearchScreenEvent {

    sealed class QueryEvent : SearchScreenEvent() {
        data class QueryFocusChanged(val isFocused: Boolean) : QueryEvent()
        data class QueryChanged(val queryText: String, val isFocused: Boolean) : QueryEvent()
    }

    sealed class SearchItemsEvent : SearchScreenEvent() {
        abstract val query: String
        abstract val website: SongsWebsite

        data class LoadingStarted(
            override val query: String,
            override val website: SongsWebsite
        ) : SearchItemsEvent()

        data class Loaded(
            override val query: String,
            override val website: SongsWebsite,
            val items: List<FoundSongModel>
        ) : SearchItemsEvent()

        data class Failed(
            override val query: String,
            override val website: SongsWebsite,
            val error: LoadSearchResultsError
        ) : SearchItemsEvent()
    }

    data class FavoriteChanged(val url: String, val isFavorite: Boolean) : SearchScreenEvent()

    data class WebsiteChanged(val website: SongsWebsite) : SearchScreenEvent()

    data class SuggestionsChanged(
        val targetQuery: String,
        val suggestionsList: List<QuerySuggestion>
    ) : SearchScreenEvent()

}