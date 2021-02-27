package com.example.pocketsongbook.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pocketsongbook.data.database.entities.SongOptionsEntity

@Dao
interface SongsOptionsDao {

    @Query("SELECT * FROM song_options WHERE song_url = :url")
    suspend fun findByUrl(url: String): List<SongOptionsEntity>

    @Query("SELECT * FROM song_options")
    suspend fun getAll(): List<SongOptionsEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(optionsEntity: SongOptionsEntity)

    @Query("DELETE FROM song_options WHERE song_url = :url")
    suspend fun deleteByUrl(url: String)
}