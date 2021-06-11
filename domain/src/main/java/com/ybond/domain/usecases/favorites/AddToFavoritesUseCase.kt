package com.ybond.domain.usecases.favorites

import com.ybond.core_entities.models.SongModel
import com.ybond.core_entities.event_bus.Event
import com.ybond.domain.repositories.FavouriteSongsRepository
import com.ybond.domain.usecases.event_bus.EventBus
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
