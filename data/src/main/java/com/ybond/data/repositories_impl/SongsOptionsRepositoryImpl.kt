package com.ybond.data.repositories_impl

import com.ybond.data_local.database.SongsOptionsDao
import com.ybond.core_entities.models.SongModel
import com.ybond.core_entities.models.SongOptionsState
import com.ybond.data.mappers.SettingsMapper
import javax.inject.Inject

class SongsOptionsRepositoryImpl @Inject constructor(
    private val songsOptionsDao: SongsOptionsDao,
    private val settingsMapper: SettingsMapper
) : com.ybond.domain.repositories.SongsOptionsRepository {

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