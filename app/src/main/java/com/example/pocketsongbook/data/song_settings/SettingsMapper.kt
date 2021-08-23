package com.example.pocketsongbook.data.song_settings

import com.example.pocketsongbook.domain.song_settings.model.SongOptionsState
import com.ybond.core_db_api.entities.SongOptionsEntity

interface SettingsMapper {

    fun map(entity: SongOptionsEntity): SongOptionsState

    fun reverseMap(url: String, state: SongOptionsState): SongOptionsEntity

}
