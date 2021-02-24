package com.example.pocketsongbook.feature.song.mvi.state_models

import com.example.pocketsongbook.domain.song.models.ChordsKey
import com.example.pocketsongbook.domain.song.models.FontSize

sealed class SongViewStateModel {
    object Loading : SongViewStateModel()

    data class Loaded(
        val songTitle: String,
        val songArtist: String,
        val rawLyrics: String,
        val isFavorite: Boolean,
        val chordsKeyOption: ChangeableOption<ChordsKey>,
        val textSizeOption: ChangeableOption<FontSize>,
        val formattedLyricsHtml: String
    ) : SongViewStateModel()
}

