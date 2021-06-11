package com.ybond.domain.usecases.favorites

import com.ybond.core_entities.event_bus.Event
import com.ybond.core_entities.models.SongModel
import com.ybond.domain.usecases.event_bus.EventBus
import javax.inject.Inject

class RemoveFromFavoritesUseCase @Inject constructor(
    private val favoritesRepository: com.ybond.domain.repositories.FavouriteSongsRepository,
    private val eventBus: EventBus
) {

    suspend operator fun invoke(song: SongModel) {
        favoritesRepository.removeSongByUrl(song.url)
        eventBus.postEvent(Event.FavoritesChange(url = song.url, isAdded = false))
    }
}
