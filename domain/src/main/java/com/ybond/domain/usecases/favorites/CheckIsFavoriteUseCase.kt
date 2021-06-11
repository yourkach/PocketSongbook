package com.ybond.domain.usecases.favorites

import com.ybond.core_entities.models.SongModel
import javax.inject.Inject

class CheckIsFavoriteUseCase @Inject constructor(
    private val favouriteSongsRepository: com.ybond.domain.repositories.FavouriteSongsRepository
) {

    suspend operator fun invoke(song: SongModel): Boolean {
        return favouriteSongsRepository.containsSong(song.url)
    }

}
