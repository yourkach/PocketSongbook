package com.example.pocketsongbook.data.song_settings

import com.ybond.core.entities.SongModel
import com.example.pocketsongbook.domain.song_settings.SongsOptionsRepository
import com.example.pocketsongbook.domain.song_settings.model.SongOptionsState
import com.ybond.core_db_api.dao.SongsOptionsDao
import javax.inject.Inject

class SongsOptionsRepositoryImpl @Inject constructor(
    private val songsOptionsDao: SongsOptionsDao,
    private val settingsMapper: SettingsMapper
) : SongsOptionsRepository {

    override suspend fun saveOptions(songUrl: String, optionsState: SongOptionsState) {
        songsOptionsDao.insert(settingsMapper.reverseMap(songUrl, optionsState))
    }

    override suspend fun getSavedOptions(songUrl: String): SongOptionsState? {
        return songsOptionsDao.findByUrl(songUrl).firstOrNull()?.let(settingsMapper::map)
    }

    override suspend fun removeSettingsForSong(song: SongModel) {
        songsOptionsDao.deleteByUrl(song.url)
    }
}