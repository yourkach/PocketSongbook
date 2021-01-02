package com.example.pocketsongbook.feature.search.usecase

import com.example.pocketsongbook.common.BaseUseCase
import com.example.pocketsongbook.data.network.WebsiteParsersManager
import javax.inject.Inject

class GetWebsiteNamesUseCase @Inject constructor(
    private val websitesManager: WebsiteParsersManager
) : BaseUseCase<Unit, GetWebsiteNamesUseCase.Response>() {

    override suspend fun execute(param: Unit): Response {
        return Response(
            websitesManager.websiteNames,
            websitesManager.selectedWebsiteName
        )
    }

    data class Response(val websiteNames: List<String>, val selectedWebsiteName: String)
}