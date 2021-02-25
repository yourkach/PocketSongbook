package com.example.pocketsongbook.feature.song.mvi

import com.example.pocketsongbook.domain.favorites.CheckIsFavoriteUseCase
import com.example.pocketsongbook.domain.models.SongModel
import com.example.pocketsongbook.domain.song.FontSizeChangeHelper
import com.example.pocketsongbook.domain.song.TransposeLyricsUseCase
import com.example.pocketsongbook.domain.song.models.ChordsKey
import com.example.pocketsongbook.domain.song.models.FontSize
import com.example.pocketsongbook.feature.song.mvi.state_models.*
import javax.inject.Inject

class GetInitialSongStateUseCase @Inject constructor(
    private val checkIsFavoriteUseCase: CheckIsFavoriteUseCase,
    private val transposeLyricsUseCase: TransposeLyricsUseCase,
    private val fontSizeChangeHelper: FontSizeChangeHelper
) {

    suspend operator fun invoke(song: SongModel): SongScreenState {
        val lyricsState = transposeLyricsUseCase(
            rawLyrics = song.lyrics,
            transposingKey = ChordsKey(key = 0)
        )
        val songState = SongViewStateModel.Loaded(
            songTitle = song.title,
            songArtist = song.artist,
            rawLyrics = song.lyrics,
            isFavorite = checkIsFavoriteUseCase(song),
            chordsKeyOption = ChangeableOption(lyricsState.chordsKey, isDefault = true),
            textSizeOption = fontSizeChangeHelper.changeFontSizeOption(FontSize(0), ChangeType.SetDefault),
            formattedLyricsHtml = lyricsState.formattedLyricsHtml
        )
        val chordsBarState = ChordBarViewStateModel(
            isOpened = false,
            chords = lyricsState.chordsList
        )
        return SongScreenState(
            songState = songState,
            chordsBarState = chordsBarState
        )
    }

}
