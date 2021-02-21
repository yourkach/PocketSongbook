package com.example.pocketsongbook.feature.search.usecase

import com.example.pocketsongbook.data.models.FoundSongModel
import com.example.pocketsongbook.data.models.SongModel
import com.example.pocketsongbook.domain.SongsRemoteRepository
import com.example.pocketsongbook.domain.favorites.FavouriteSongsRepository
import javax.inject.Inject

class LoadSongModelUseCase @Inject constructor(
    private val songsRemoteRepository: SongsRemoteRepository,
    private val favouriteSongsRepository: FavouriteSongsRepository
) {

    suspend operator fun invoke(foundSong: FoundSongModel): SongModel? {
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