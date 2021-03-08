package com.example.pocketsongbook.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pocketsongbook.data.database.entities.FavoriteSongEntity
import com.example.pocketsongbook.data.database.entities.SavedQueryEntity

@Dao
interface SavedQueriesDao {

    @Query("SELECT * FROM saved_search_queries ORDER BY saved_at DESC")
    suspend fun getAll(): List<SavedQueryEntity>

    @Query("SELECT * FROM saved_search_queries WHERE text LIKE  :startsWith || '%' ")
    suspend fun getMatchingQueries(startsWith: String): List<SavedQueryEntity>

    @Query("DELETE FROM saved_search_queries")
    suspend fun clearAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favoriteSong: FavoriteSongEntity)

}