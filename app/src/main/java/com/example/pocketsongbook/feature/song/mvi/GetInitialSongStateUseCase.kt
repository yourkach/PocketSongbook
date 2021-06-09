package com.example.pocketsongbook.feature.song.mvi

import com.example.pocketsongbook.domain.favorites.CheckIsFavoriteUseCase
import com.ybond.core.models.SongModel
import com.example.pocketsongbook.domain.song.GetLyricsStateUseCase
import com.example.pocketsongbook.domain.song_settings.usecase.GetSongSettingsUseCase
import com.example.pocketsongbook.feature.song.mvi.state_models.ChordBarViewStateModel
import com.example.pocketsongbook.feature.song.mvi.state_models.SongScreenState
import com.example.pocketsongbook.feature.song.mvi.state_models.SongViewStateModel
import javax.inject.Inject

class GetInitialSongStateUseCase @Inject constructor(
    private val checkIsFavoriteUseCase: CheckIsFavoriteUseCase,
    private val getLyricsStateUseCase: GetLyricsStateUseCase,
    private val getSongOptionsUseCase: GetSongSettingsUseCase
) {

    suspend operator fun invoke(songModel: SongModel): SongScreenState {
        val optionsState = getSongOptionsUseCase(songModel)
        val lyricsState = getLyricsStateUseCase(
            rawLyrics = songModel.lyrics,
            transposingKey = optionsState.chordsOption.selectedValue
        )
        val songState = SongViewStateModel.Loaded(
            songUrl = songModel.url,
            songTitle = songModel.title,
            songArtist = songModel.artist,
            rawLyrics = songModel.lyrics,
            isFavorite = checkIsFavoriteUseCase(songModel),
            optionsState = optionsState,
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
