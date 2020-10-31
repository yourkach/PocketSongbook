package com.example.pocketsongbook.feature.search.usecase

import com.example.pocketsongbook.common.BaseUseCase
import com.example.pocketsongbook.data.network.WebsitesManager
import javax.inject.Inject

class SwitchToWebSiteUseCase @Inject constructor(
    private val websitesManager: WebsitesManager
) : BaseUseCase<SwitchToWebSiteUseCase.Param, SwitchToWebSiteUseCase.Response>() {

    override suspend fun execute(param: Param): Response {
        return Response(
            switchSuccessful = websitesManager.selectByName(
                websiteName = param.websiteName
            )
        )
    }

    data class Param(val websiteName: String)

    data class Response(val switchSuccessful: Boolean)

}