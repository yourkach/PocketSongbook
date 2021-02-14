package com.example.pocketsongbook.domain.favorites

import com.example.pocketsongbook.data.favourites.FavouriteSongsRepository
import com.example.pocketsongbook.data.models.SongModel
import com.example.pocketsongbook.domain.event_bus.EventBus
import javax.inject.Inject

class CheckIsFavoriteUseCase @Inject constructor(
    private val favouriteSongsRepository: FavouriteSongsRepository
) {

    suspend operator fun invoke(song: SongModel): Boolean {
        return favouriteSongsRepository.containsSong(song.url)
    }

}
