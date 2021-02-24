package com.example.pocketsongbook.feature.song.mvi.state_mappers

import com.example.pocketsongbook.domain.song.FontSizeChangeHelper
import com.example.pocketsongbook.feature.song.mvi.SongStateByEventMapper
import com.example.pocketsongbook.feature.song.mvi.state_models.SongScreenInteractionEvent
import com.example.pocketsongbook.feature.song.mvi.state_models.SongScreenState
import com.example.pocketsongbook.feature.song.mvi.state_models.SongViewStateModel
import javax.inject.Inject

class MapStateForChangeFontSize @Inject constructor(
    private val fontSizeChangeHelper: FontSizeChangeHelper
) : SongStateByEventMapper<SongScreenInteractionEvent.ChangeFontSize>() {
    override suspend operator fun invoke(
        currentState: SongScreenState,
        event: SongScreenInteractionEvent.ChangeFontSize
    ): SongScreenState {
        return (currentState.songState as? SongViewStateModel.Loaded)?.let { songState ->
            val newSizeOption = fontSizeChangeHelper.changeFontSizeOption(
                currentSize = songState.textSizeOption.selectedValue,
                changeType = event.changeType
            )
            currentState.copy(
                songState = songState.copy(textSizeOption = newSizeOption)
            )
        } ?: currentState
    }
}


