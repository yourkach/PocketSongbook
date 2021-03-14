package com.example.pocketsongbook.feature.search.usecase

import com.example.pocketsongbook.domain.favorites.FavouriteSongsRepository
import com.example.pocketsongbook.domain.models.FoundSongModel
import com.example.pocketsongbook.domain.search.SongsRemoteRepository
import com.example.pocketsongbook.domain.search.SongsWebsite
import javax.inject.Inject

class GetSearchResultsUseCase @Inject constructor(
    private val websitesManager: SongsRemoteRepository,
    private val favouriteSongsRepository: FavouriteSongsRepository
) {

    suspend operator fun invoke(website: SongsWebsite, query: String): List<FoundSongModel> {
        return websitesManager.loadSearchResults(website, query).onEach {
            it.isFavourite = favouriteSongsRepository.containsSong(it.url)
        }
    }

}