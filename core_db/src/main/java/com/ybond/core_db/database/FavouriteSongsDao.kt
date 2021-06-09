package com.ybond.core_db.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ybond.core_db.database.entities.FavoriteSongEntity

@Dao
interface FavouriteSongsDao {

    @Query("SELECT * FROM favorite_songs ORDER BY time_added DESC")
    suspend fun getAll(): List<FavoriteSongEntity>

    @Query("SELECT * FROM favorite_songs WHERE artist LIKE  '%' || :query || '%' OR title LIKE '%' || :query || '%' ")
    suspend fun findByQuery(query: String): List<FavoriteSongEntity>

    @Query("SELECT * FROM favorite_songs WHERE url = :url")
    suspend fun findByUrl(url: String): List<FavoriteSongEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favoriteSong: FavoriteSongEntity)

    @Query("DELETE FROM favorite_songs WHERE url = :url")
    suspend fun deleteByUrl(url: String)
}