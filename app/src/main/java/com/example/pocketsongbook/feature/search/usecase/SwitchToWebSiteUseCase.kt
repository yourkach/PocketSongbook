package com.example.pocketsongbook.feature.search.usecase

import com.example.pocketsongbook.common.BaseUseCase
import com.example.pocketsongbook.data.network.WebsitesManager
import javax.inject.Inject

class SwitchToWebSiteUseCase @Inject constructor(
    private val websitesManager: WebsitesManager
) : BaseUseCase<Int, Boolean>() {

    //todo возможно стоит возвращать Bool как результат переключения

    override suspend fun execute(param: Int) : Boolean {
        return websitesManager.switchToWebsite(param)
    }

}