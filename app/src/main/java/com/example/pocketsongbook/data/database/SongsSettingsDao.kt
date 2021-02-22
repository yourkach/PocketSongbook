package com.example.pocketsongbook.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.pocketsongbook.data.database.entities.FavoriteSongEntity
import com.example.pocketsongbook.data.database.entities.SongSettingsEntity

@Dao
interface SongsSettingsDao {

    @Query("SELECT * FROM song_settings WHERE song_url = :url")
    suspend fun findByUrl(url: String): List<SongSettingsEntity>

    @Insert
    suspend fun insert(settingsEntity: SongSettingsEntity)

    @Query("DELETE FROM song_settings WHERE song_url = :url")
    suspend fun deleteByUrl(url: String)
}