package com.example.pocketsongbook.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.pocketsongbook.data.database.entities.FavoriteSongEntity

@Dao
interface FavouriteSongsDao {

    @Query("SELECT * FROM favorite_songs ORDER BY time_added DESC")
    fun getAll(): List<FavoriteSongEntity>

    @Query("SELECT * FROM favorite_songs WHERE artist LIKE :name || '%' OR title LIKE :name")
    fun findByName(name: String): List<FavoriteSongEntity>

    @Query("SELECT * FROM favorite_songs WHERE url = :url")
    fun findByUrl(url: String): List<FavoriteSongEntity>

    @Insert
    fun insert(favoriteSong: FavoriteSongEntity)

    @Query("DELETE FROM favorite_songs WHERE url = :url")
    fun deleteByUrl(url: String)
}