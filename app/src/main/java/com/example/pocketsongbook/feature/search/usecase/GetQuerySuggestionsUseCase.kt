package com.example.pocketsongbook.feature.search.usecase

import com.example.pocketsongbook.domain.search.SavedSearchQueryRepository
import javax.inject.Inject

class GetQuerySuggestionsUseCase @Inject constructor(
    private val searchQueryRepository: SavedSearchQueryRepository
) {

    suspend operator fun invoke(query: String): List<String> {
        return searchQueryRepository.getMatchingQueries(query)
            .sortedByDescending { it.savedAt }
            .map { it.text }
    }

}

