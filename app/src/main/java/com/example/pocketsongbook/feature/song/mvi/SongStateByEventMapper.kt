package com.example.pocketsongbook.feature.song.mvi

import com.example.pocketsongbook.feature.song.mvi.state_models.SongScreenInteractionEvent
import com.example.pocketsongbook.feature.song.mvi.state_models.SongScreenState

abstract class SongStateByEventMapper<in T : SongScreenInteractionEvent> {
    abstract suspend operator fun invoke(currentState: SongScreenState, event: T): SongScreenState
}