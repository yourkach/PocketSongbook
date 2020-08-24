package com.example.pocketsongbook.ui.fragments.search.usecase

import com.example.pocketsongbook.data.BaseUseCase
import com.example.pocketsongbook.data.api.SongsApiManager
import javax.inject.Inject

class GetWebsiteNamesUseCase @Inject constructor(
    private val songsApiManager: SongsApiManager
) : BaseUseCase<Unit, GetWebsiteNamesUseCase.Response>() {

    override suspend operator fun invoke(param: Unit): Response {
        return Response(
            songsApiManager.getWebsiteNames(),
            songsApiManager.selectedWebsitePosition
        )
    }

    data class Response(val websiteNames: List<String>, val selectedWebsitePosition: Int)
}