package com.example.pocketsongbook.ui.fragments.search.interactor

import com.example.pocketsongbook.data.BaseUseCase
import com.example.pocketsongbook.data.api.SongsApiManager
import com.example.pocketsongbook.data.models.SongSearchItem
import javax.inject.Inject

class GetSearchResultsUseCase @Inject constructor(
    private val songsApiManager: SongsApiManager
) : BaseUseCase<String, List<SongSearchItem>?>() {

    override suspend operator fun invoke(param: String): List<SongSearchItem>? {
        return songsApiManager.getSearchResults(param)
    }

}