package com.ybond.domain.repositories

import com.ybond.core_entities.models.SongModel
import com.ybond.core_entities.models.SongOptionsState

interface SongsOptionsRepository {

    suspend fun saveOptions(songUrl: String, optionsState: SongOptionsState)

    suspend fun getSavedOptions(songUrl: String): SongOptionsState?

    suspend fun removeSettingsForSong(song: SongModel)

}