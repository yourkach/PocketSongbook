package com.example.pocketsongbook.feature.search.usecase

import com.example.pocketsongbook.data.favourites.FavouriteSongsRepository
import com.example.pocketsongbook.domain.WebSongsRepository
import com.example.pocketsongbook.data.models.FoundSongModel
import com.example.pocketsongbook.domain.SongsWebsite
import javax.inject.Inject

class GetSearchResultsUseCase @Inject constructor(
    private val websitesManager: WebSongsRepository,
    private val favouriteSongsRepository: FavouriteSongsRepository
) {

    suspend operator fun invoke(website: SongsWebsite, query: String): List<FoundSongModel> {
        return websitesManager.loadSearchResults(website, query).onEach {
            it.isFavourite = favouriteSongsRepository.containsSong(it.url)
        }
    }

}