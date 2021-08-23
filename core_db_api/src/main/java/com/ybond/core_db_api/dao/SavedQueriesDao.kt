package com.ybond.core_db_api.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ybond.core_db_api.entities.SavedQueryEntity

@Dao
interface SavedQueriesDao {

    @Query("SELECT * FROM saved_search_queries ORDER BY saved_at DESC")
    suspend fun getAll(): List<SavedQueryEntity>

    @Query("SELECT * FROM saved_search_queries WHERE text LIKE  :startsWith || '%' ")
    suspend fun getMatchingQueries(startsWith: String): List<SavedQueryEntity>

    @Query("DELETE FROM saved_search_queries")
    suspend fun clearAll()

    @Query("DELETE FROM saved_search_queries WHERE text = :text")
    suspend fun deleteMatching(text: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(query: SavedQueryEntity)

}