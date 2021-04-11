package com.example.pocketsongbook.feature.search.usecase

import com.example.pocketsongbook.domain.search.SavedSearchQueryRepository
import javax.inject.Inject

class DeleteQuerySuggestionUseCase @Inject constructor(
    private val savedSearchQueryRepository: SavedSearchQueryRepository
) {

    suspend operator fun invoke(suggestionText: String) {
        savedSearchQueryRepository.deleteQuery(queryText = suggestionText)
    }

}
