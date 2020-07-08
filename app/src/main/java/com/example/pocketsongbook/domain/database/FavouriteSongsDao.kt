package com.example.pocketsongbook.domain.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.pocketsongbook.domain.database.SongEntity

@Dao
interface FavouriteSongsDao {

    @Query("SELECT * FROM songs ORDER BY time_added DESC")
    fun getAll(): List<SongEntity>

    @Query("SELECT * FROM songs WHERE artist LIKE :name || '%' OR title LIKE :name || '%' ORDER BY time_added DESC")
    fun findByName(name: String): List<SongEntity>

    @Query("SELECT * FROM songs WHERE link = :url")
    fun findByUrl(url: String): List<SongEntity>

    @Insert
    fun insert(song: SongEntity)

    @Query("DELETE FROM songs WHERE link = :url")
    fun deleteByUrl(url: String)
}