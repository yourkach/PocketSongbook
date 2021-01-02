package com.example.pocketsongbook.feature.search.usecase

import com.example.pocketsongbook.common.BaseUseCase
import com.example.pocketsongbook.data.network.WebsiteParsersManager
import javax.inject.Inject

class SelectWebsiteByNameUseCase @Inject constructor(
    private val websitesManager: WebsiteParsersManager
) : BaseUseCase<String, Boolean>() {

    override suspend fun execute(param: String): Boolean {
        return websitesManager.selectWebsiteByName(
            websiteName = param
        )
    }

}