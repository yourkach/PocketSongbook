package com.example.pocketsongbook.feature.song.mvi.state_mappers

import com.example.pocketsongbook.feature.song.mvi.SongStateByEventMapper
import com.example.pocketsongbook.feature.song.mvi.state_models.SongScreenInteractionEvent
import com.example.pocketsongbook.feature.song.mvi.state_models.SongScreenState
import com.example.pocketsongbook.feature.song.mvi.state_models.SongViewStateModel
import javax.inject.Inject

class MapStateForNewFavoriteStatus @Inject constructor() :
    SongStateByEventMapper<SongScreenInteractionEvent.FavoriteStatusChanged>() {

    override suspend operator fun invoke(
        currentState: SongScreenState,
        event: SongScreenInteractionEvent.FavoriteStatusChanged
    ): SongScreenState {
        return (currentState.songState as? SongViewStateModel.Loaded)?.let { songState ->
            val newSongState = songState.copy(isFavorite = event.isFavorite)
            currentState.copy(songState = newSongState)
        } ?: currentState
    }

}
