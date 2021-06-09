package com.example.pocketsongbook.domain.favorites

import com.ybond.core.event_bus.Event
import com.example.pocketsongbook.domain.event_bus.EventBus
import com.ybond.core.models.SongModel
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
