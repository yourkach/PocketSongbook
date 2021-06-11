package com.example.pocketsongbook.feature.search.usecase

import com.ybond.domain.repositories.FavouriteSongsRepository
import com.ybond.core_entities.models.FoundSongModel
import com.ybond.domain.repositories.SongsRemoteRepository
import com.ybond.core_entities.models.SongsWebsite
import javax.inject.Inject

class GetSearchResultsUseCase @Inject constructor(
    private val songsRemoteRepository: com.ybond.domain.repositories.SongsRemoteRepository,
    private val favouriteSongsRepository: com.ybond.domain.repositories.FavouriteSongsRepository,
    private val saveSearchQuery: SaveSearchQueryUseCase
) {

    suspend operator fun invoke(website: SongsWebsite, query: String): List<FoundSongModel> {
        return songsRemoteRepository.loadSearchResults(website, query).onEach { foundSongModel ->
            foundSongModel.copy(
                isFavourite = favouriteSongsRepository.containsSong(foundSongModel.url)
            )
        }.also { results ->
            if (results.isNotEmpty()) saveSearchQuery(query)
        }
    }

}