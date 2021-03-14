package com.example.pocketsongbook.feature.search.usecase

import com.example.pocketsongbook.domain.search.SavedSearchQueryRepository
import javax.inject.Inject

class SaveSearchQueryUseCase @Inject constructor(
    private val searchQueryRepository: SavedSearchQueryRepository
) {

    suspend operator fun invoke(queryText: String) {
        searchQueryRepository.saveQuery(queryText)
    }

}