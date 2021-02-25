package com.example.pocketsongbook.feature.song.mvi

import com.example.pocketsongbook.feature.song.mvi.state_mappers.MapStateForChangeChordsKey
import com.example.pocketsongbook.feature.song.mvi.state_mappers.MapStateForChangeFontSize
import com.example.pocketsongbook.feature.song.mvi.state_mappers.MapStateForChordsButtonClick
import com.example.pocketsongbook.feature.song.mvi.state_mappers.MapStateForNewFavoriteStatus
import com.example.pocketsongbook.feature.song.mvi.state_models.SongScreenInteractionEvent
import com.example.pocketsongbook.feature.song.mvi.state_models.SongScreenState
import javax.inject.Inject

class GetUpdatedStateUseCase @Inject constructor(
    private val mapStateForChangeFontSize: MapStateForChangeFontSize,
    private val mapStateForChangeChordsKey: MapStateForChangeChordsKey,
    private val mapStateForNewFavoriteStatus: MapStateForNewFavoriteStatus,
    private val mapStateForChordsButtonClick: MapStateForChordsButtonClick
) {

    suspend operator fun invoke(
        currentState: SongScreenState,
        interactionEvent: SongScreenInteractionEvent
    ): SongScreenState {
        return when (interactionEvent) {
            is SongScreenInteractionEvent.ChangeFontSize -> {
                mapStateForChangeFontSize(currentState, interactionEvent)
            }
            is SongScreenInteractionEvent.ChangeChordsKey -> {
                mapStateForChangeChordsKey(currentState, interactionEvent)
            }
            is SongScreenInteractionEvent.FavoriteStatusChanged -> {
                mapStateForNewFavoriteStatus(currentState, interactionEvent)
            }
            is SongScreenInteractionEvent.ChordsBarButtonClick -> {
                mapStateForChordsButtonClick(currentState, interactionEvent)
            }
        }
    }
}

