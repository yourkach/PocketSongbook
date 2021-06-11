package com.example.pocketsongbook.feature.search.usecase

import com.ybond.domain.repositories.SavedSearchQueryRepository
import javax.inject.Inject

class SaveSearchQueryUseCase @Inject constructor(
    private val searchSavedSearchQueryRepository: com.ybond.domain.repositories.SavedSearchQueryRepository
) {

    suspend operator fun invoke(queryText: String) {
        searchSavedSearchQueryRepository.saveQuery(queryText)
    }

}