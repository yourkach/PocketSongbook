package com.example.pocketsongbook.domain.favorites

import com.example.pocketsongbook.domain.event_bus.Event
import com.example.pocketsongbook.domain.event_bus.EventBus
import com.ybond.core.entities.SongModel
import javax.inject.Inject

class AddToFavoritesUseCase @Inject constructor(
    private val favoritesRepository: FavouriteSongsRepository,
    private val eventBus: EventBus
) {

    suspend operator fun invoke(song: SongModel) {
        favoritesRepository.addSong(song)
        eventBus.postEvent(Event.FavoritesChange(url = song.url, isAdded = true))
    }

}
