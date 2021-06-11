package com.example.pocketsongbook.feature.search.usecase

import com.ybond.domain.repositories.SavedSearchQueryRepository
import javax.inject.Inject

class DeleteQuerySuggestionUseCase @Inject constructor(
    private val savedSearchQueryRepository: com.ybond.domain.repositories.SavedSearchQueryRepository
) {

    suspend operator fun invoke(suggestionText: String) {
        savedSearchQueryRepository.deleteQuery(queryText = suggestionText)
    }

}
