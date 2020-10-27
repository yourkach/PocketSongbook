package com.example.pocketsongbook.feature.search.usecase

import com.example.pocketsongbook.common.BaseUseCase
import com.example.pocketsongbook.data.network.WebsitesManager
import javax.inject.Inject

class GetWebsiteNamesUseCase @Inject constructor(
    private val websitesManager: WebsitesManager
) : BaseUseCase<Unit, GetWebsiteNamesUseCase.Response>() {

    override suspend fun execute(param: Unit): Response {
        return Response(
            websitesManager.getWebsiteNames(),
            websitesManager.selectedWebsitePosition
        )
    }

    data class Response(val websiteNames: List<String>, val selectedWebsitePosition: Int)
}