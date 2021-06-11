package com.ybond.domain.usecases.favorites

import com.ybond.core_entities.event_bus.Event
import com.ybond.core_entities.models.SongModel
import com.ybond.domain.usecases.event_bus.EventBus
import javax.inject.Inject

class ToggleSongFavoriteStatusUseCase @Inject constructor(
    private val favoritesRepository: com.ybond.domain.repositories.FavouriteSongsRepository,
    private val eventBus: EventBus
) {
    suspend operator fun invoke(song: SongModel) {
        val isAdded = if (favoritesRepository.containsSong(song.url)) {
            favoritesRepository.removeSongByUrl(song.url)
            false
        } else {
            favoritesRepository.addSong(song)
            true
        }
        eventBus.postEvent(Event.FavoritesChange(isAdded = isAdded, url = song.url))
    }
}