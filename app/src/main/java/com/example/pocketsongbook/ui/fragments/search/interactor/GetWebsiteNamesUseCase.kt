package com.example.pocketsongbook.ui.fragments.search.interactor

import com.example.pocketsongbook.domain.BaseUseCase
import com.example.pocketsongbook.domain.api.SongsApiManager
import javax.inject.Inject

class GetWebsiteNamesUseCase @Inject constructor(
    private val songsApiManager: SongsApiManager
) : BaseUseCase<Unit, List<String>>() {

    override suspend operator fun invoke(param: Unit): List<String> {
        return songsApiManager.getWebsiteNames()
    }

}