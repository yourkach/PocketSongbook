package com.example.pocketsongbook.feature.search.usecase

import com.example.pocketsongbook.common.BaseUseCase
import com.example.pocketsongbook.data.favourites.FavouriteSongsRepo
import com.example.pocketsongbook.data.network.WebsiteParsersManager
import com.example.pocketsongbook.data.models.SongSearchItem
import javax.inject.Inject

class GetSearchResultsUseCase @Inject constructor(
    private val websitesManager: WebsiteParsersManager,
    private val favouriteSongsRepo: FavouriteSongsRepo
) : BaseUseCase<String, List<SongSearchItem>>() {

    override suspend fun execute(param: String): List<SongSearchItem> {
        return websitesManager.loadSearchResults(param).onEach {
            it.isFavourite = favouriteSongsRepo.containsSong(it.url)
        }
    }

}