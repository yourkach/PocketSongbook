package com.example.pocketsongbook.feature.search.usecase

import com.example.pocketsongbook.domain.search.SavedSearchQueryRepository
import com.example.pocketsongbook.feature.search.QuerySuggestion
import javax.inject.Inject

class GetQuerySuggestionsUseCase @Inject constructor(
    private val searchSavedSearchQueryRepository: SavedSearchQueryRepository
) {

    suspend operator fun invoke(query: String): List<QuerySuggestion> {
        return searchSavedSearchQueryRepository.getMatchingQueries(query)
            .sortedByDescending { it.savedAt }
            .map { QuerySuggestion(it.text) }
    }

}

