package com.example.pocketsongbook.ui.fragments.search.interactor

import com.example.pocketsongbook.domain.BaseUseCase
import com.example.pocketsongbook.domain.api.SongsApiManager
import com.example.pocketsongbook.domain.models.SongSearchItem
import javax.inject.Inject

class GetSearchResultsUseCase @Inject constructor(
    private val songsApiManager: SongsApiManager
) : BaseUseCase<String, List<SongSearchItem>?>() {

    override suspend operator fun invoke(param: String): List<SongSearchItem>? {
        return songsApiManager.getSearchResults(param)
    }

}