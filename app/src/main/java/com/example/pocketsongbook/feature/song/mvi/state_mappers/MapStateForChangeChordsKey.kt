package com.example.pocketsongbook.feature.song.mvi.state_mappers

import com.example.pocketsongbook.domain.song.ChordsKeyChangeHelper
import com.example.pocketsongbook.domain.song.GetLyricsStateUseCase
import com.example.pocketsongbook.feature.song.mvi.SongStateByEventMapper
import com.example.pocketsongbook.feature.song.mvi.state_models.SongScreenInteractionEvent
import com.example.pocketsongbook.feature.song.mvi.state_models.SongScreenState
import com.example.pocketsongbook.feature.song.mvi.state_models.SongViewStateModel
import javax.inject.Inject

class MapStateForChangeChordsKey @Inject constructor(
    private val getLyricsStateUseCase: GetLyricsStateUseCase,
    private val chordsKeyChangeHelper: ChordsKeyChangeHelper
) : SongStateByEventMapper<SongScreenInteractionEvent.ChangeChordsKey>() {
    override suspend operator fun invoke(
        currentState: SongScreenState,
        event: SongScreenInteractionEvent.ChangeChordsKey
    ): SongScreenState {
        return (currentState.songState as? SongViewStateModel.Loaded)?.let { songState ->
            val newChordsKeyOption = chordsKeyChangeHelper.changeChordsKeyOption(
                songState.optionsState.chordsOption.selectedValue,
                event.changeType
            )
            val transposeLyricsResponse = getLyricsStateUseCase(
                songState.rawLyrics,
                newChordsKeyOption.selectedValue
            )
            currentState.copy(
                songState = songState.copy(
                    formattedLyricsHtml = transposeLyricsResponse.formattedLyricsHtml,
                    optionsState = songState.optionsState.copy(
                        chordsOption = newChordsKeyOption
                    )
                ),
                chordsBarState = currentState.chordsBarState.copy(
                    chords = transposeLyricsResponse.chordsList
                )
            )
        } ?: currentState
    }
}