package com.example.pocketsongbook.domain.song_settings

import com.ybond.core.models.SongModel
import com.example.pocketsongbook.domain.song_settings.model.SongOptionsState

interface SongsOptionsRepository {

    suspend fun saveOptions(songUrl: String, optionsState: SongOptionsState)

    suspend fun getSavedOptions(songUrl: String): SongOptionsState?

    suspend fun removeSettingsForSong(song: SongModel)

}