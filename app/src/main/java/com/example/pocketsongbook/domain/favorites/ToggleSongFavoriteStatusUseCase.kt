package com.example.pocketsongbook.domain.favorites

import com.example.pocketsongbook.domain.event_bus.Event
import com.example.pocketsongbook.domain.event_bus.EventBus
import com.example.pocketsongbook.domain.models.SongModel
import javax.inject.Inject

class ToggleSongFavoriteStatusUseCase @Inject constructor(
    private val favoritesRepository: FavouriteSongsRepository,
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