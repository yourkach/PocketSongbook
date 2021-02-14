package com.example.pocketsongbook.feature.search.usecase

import com.example.pocketsongbook.domain.WebSongsRepository
import com.example.pocketsongbook.data.favourites.FavouriteSongsRepository
import com.example.pocketsongbook.data.models.SongModel
import com.example.pocketsongbook.data.models.FoundSongModel
import javax.inject.Inject

class LoadSongModelUseCase @Inject constructor(
    private val websitesManager: WebSongsRepository,
    private val favouriteSongsRepository: FavouriteSongsRepository
) {

    suspend operator fun invoke(foundSong: FoundSongModel): SongModel? {
        return foundSong.isFavourite
            .takeIf { it }
            ?.let {
                favouriteSongsRepository.findSongByUrl(foundSong.url)?.toSong()
            }
            ?: let {
                websitesManager.loadSong(foundSong)
            }
    }
}