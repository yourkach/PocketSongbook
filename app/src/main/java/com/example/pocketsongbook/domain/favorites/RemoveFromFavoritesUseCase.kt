package com.example.pocketsongbook.domain.favorites

import com.example.pocketsongbook.data.models.SongModel
import com.example.pocketsongbook.domain.event_bus.Event
import com.example.pocketsongbook.domain.event_bus.EventBus
import javax.inject.Inject

class RemoveFromFavoritesUseCase @Inject constructor(
    private val favoritesRepository: FavouriteSongsRepository,
    private val eventBus: EventBus
) {

    suspend operator fun invoke(song: SongModel) {
        favoritesRepository.removeSongByUrl(song.url)
        eventBus.postEvent(Event.FavoritesChange(url = song.url, isAdded = false))
    }
}
