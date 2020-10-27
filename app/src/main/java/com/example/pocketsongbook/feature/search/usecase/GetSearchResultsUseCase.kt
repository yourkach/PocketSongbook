package com.example.pocketsongbook.feature.search.usecase

import com.example.pocketsongbook.common.BaseUseCase
import com.example.pocketsongbook.data.network.WebsitesManager
import com.example.pocketsongbook.data.models.SongSearchItem
import javax.inject.Inject

class GetSearchResultsUseCase @Inject constructor(
    private val websitesManager: WebsitesManager
) : BaseUseCase<String, List<SongSearchItem>?>() {

    override suspend fun execute(param: String): List<SongSearchItem>? {
        return websitesManager.getSearchResults(param)
    }

}