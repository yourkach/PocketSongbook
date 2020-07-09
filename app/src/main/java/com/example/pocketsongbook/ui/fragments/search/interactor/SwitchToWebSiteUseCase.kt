package com.example.pocketsongbook.ui.fragments.search.interactor

import com.example.pocketsongbook.data.BaseUseCase
import com.example.pocketsongbook.data.api.SongsApiManager
import javax.inject.Inject

class SwitchToWebSiteUseCase @Inject constructor(
    private val songsApiManager: SongsApiManager
) : BaseUseCase<Int, Boolean>() {

    //todo возможно стоит возвращать Bool как результат переключения

    override suspend operator fun invoke(param: Int) : Boolean {
        return songsApiManager.switchToWebsite(param)
    }

}