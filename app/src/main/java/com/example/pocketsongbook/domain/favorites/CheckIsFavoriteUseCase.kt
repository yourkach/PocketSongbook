package com.example.pocketsongbook.domain.favorites

import com.ybond.core.models.SongModel
import javax.inject.Inject

class CheckIsFavoriteUseCase @Inject constructor(
    private val favouriteSongsRepository: FavouriteSongsRepository
) {

    suspend operator fun invoke(song: SongModel): Boolean {
        return favouriteSongsRepository.containsSong(song.url)
    }

}
