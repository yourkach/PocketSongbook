package com.example.pocketsongbook.feature.search.usecase

import com.ybond.domain.repositories.FavouriteSongsRepository
import com.ybond.core_entities.models.FoundSongModel
import com.ybond.core_entities.models.SongModel
import com.ybond.domain.repositories.SongsRemoteRepository
import javax.inject.Inject

class LoadSongModelUseCase @Inject constructor(
    private val songsRemoteRepository: com.ybond.domain.repositories.SongsRemoteRepository,
    private val favouriteSongsRepository: com.ybond.domain.repositories.FavouriteSongsRepository
) {

    suspend operator fun invoke(foundSong: FoundSongModel): SongModel {
        return foundSong.isFavourite
            .takeIf { it }
            ?.let {
                favouriteSongsRepository.findSongByUrl(foundSong.url)?.song
            }
            ?: let {
                songsRemoteRepository.loadSong(foundSong)
            }
    }
}