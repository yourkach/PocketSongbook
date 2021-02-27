package com.example.pocketsongbook.data.song_settings

import com.example.pocketsongbook.data.database.entities.SongOptionsEntity
import com.example.pocketsongbook.domain.song_settings.model.SongOptionsState

interface SettingsMapper {

    fun map(entity: SongOptionsEntity): SongOptionsState

    fun reverseMap(url: String, state: SongOptionsState): SongOptionsEntity

}
