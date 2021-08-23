package com.example.pocketsongbook.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ybond.core_db_api.dao.FavoriteSongsDao
import com.ybond.core_db_api.dao.SavedQueriesDao
import com.ybond.core_db_api.dao.SongsOptionsDao
import com.ybond.core_db_api.entities.FavoriteSongEntity
import com.ybond.core_db_api.entities.SavedQueryEntity
import com.ybond.core_db_api.entities.SongOptionsEntity

@Database(
    entities = [
        FavoriteSongEntity::class,
        SongOptionsEntity::class,
        SavedQueryEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteSongsDao(): FavoriteSongsDao
    abstract fun songsOptionsDao(): SongsOptionsDao
    abstract fun savedQueriesDao(): SavedQueriesDao
}