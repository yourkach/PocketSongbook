package com.example.pocketsongbook.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.pocketsongbook.data.database.entities.SongEntity

@Dao
interface FavouriteSongsDao {

    @Query("SELECT * FROM songs ORDER BY time_added DESC")
    fun getAll(): List<SongEntity>

    @Query("SELECT * FROM songs WHERE artist LIKE :name || '%' OR title LIKE :name")
    fun findByName(name: String): List<SongEntity>

    @Query("SELECT * FROM songs WHERE url = :url")
    fun findByUrl(url: String): List<SongEntity>

    @Insert
    fun insert(song: SongEntity)

    @Query("DELETE FROM songs WHERE url = :url")
    fun deleteByUrl(url: String)
}