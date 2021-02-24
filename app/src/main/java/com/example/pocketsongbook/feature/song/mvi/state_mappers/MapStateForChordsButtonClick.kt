package com.example.pocketsongbook.feature.song.mvi.state_mappers

import com.example.pocketsongbook.feature.song.mvi.SongStateByEventMapper
import com.example.pocketsongbook.feature.song.mvi.state_models.SongScreenInteractionEvent
import com.example.pocketsongbook.feature.song.mvi.state_models.SongScreenState
import javax.inject.Inject

class MapStateForChordsButtonClick @Inject constructor() :
    SongStateByEventMapper<SongScreenInteractionEvent.ChordsBarButtonClick>() {

    override suspend operator fun invoke(
        currentState: SongScreenState,
        event: SongScreenInteractionEvent.ChordsBarButtonClick
    ): SongScreenState {
        val newState = currentState.chordsBarState.run {
            copy(isOpened = !isOpened)
        }
        return currentState.copy(
            chordsBarState = newState
        )
    }

}
