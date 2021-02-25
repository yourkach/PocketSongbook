package com.example.pocketsongbook.feature.song.mvi.state_mappers

import com.example.pocketsongbook.domain.song.ChordsKeyChangeHelper
import com.example.pocketsongbook.domain.song.TransposeLyricsUseCase
import com.example.pocketsongbook.feature.song.mvi.SongStateByEventMapper
import com.example.pocketsongbook.feature.song.mvi.state_models.SongScreenInteractionEvent
import com.example.pocketsongbook.feature.song.mvi.state_models.SongScreenState
import com.example.pocketsongbook.feature.song.mvi.state_models.SongViewStateModel
import javax.inject.Inject

class MapStateForChangeChordsKey @Inject constructor(
    private val transposeLyricsUseCase: TransposeLyricsUseCase,
    private val chordsKeyChangeHelper: ChordsKeyChangeHelper
) : SongStateByEventMapper<SongScreenInteractionEvent.ChangeChordsKey>() {
    override suspend operator fun invoke(
        currentState: SongScreenState,
        event: SongScreenInteractionEvent.ChangeChordsKey
    ): SongScreenState {
        return (currentState.songState as? SongViewStateModel.Loaded)?.let { songState ->
            val newKeyOption = chordsKeyChangeHelper.changeChordsKeyOption(
                songState.chordsKeyOption.selectedValue,
                event.changeType
            )
            val transposeLyricsResponse = transposeLyricsUseCase(
                songState.rawLyrics,
                newKeyOption.selectedValue
            )
            currentState.copy(
                songState = songState.copy(
                    formattedLyricsHtml = transposeLyricsResponse.formattedLyricsHtml,
                    chordsKeyOption = newKeyOption
                ),
                chordsBarState = currentState.chordsBarState.copy(
                    chords = transposeLyricsResponse.chordsList
                )
            )
        } ?: currentState
    }
}