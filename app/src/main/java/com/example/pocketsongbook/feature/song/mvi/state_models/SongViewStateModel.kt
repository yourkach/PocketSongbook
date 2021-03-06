package com.example.pocketsongbook.feature.song.mvi.state_models

import com.example.pocketsongbook.domain.song_settings.model.SongOptionsState

sealed class SongViewStateModel {
    object Loading : SongViewStateModel()

    data class Loaded(
        val songUrl: String,
        val songTitle: String,
        val songArtist: String,
        val rawLyrics: String,
        val isFavorite: Boolean,
        val optionsState: SongOptionsState,
        val formattedLyricsHtml: String
    ) : SongViewStateModel()
}

fun SongViewStateModel.asLoadedOrNull() : SongViewStateModel.Loaded? =
    (this as? SongViewStateModel.Loaded)
