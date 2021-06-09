package com.ybond.core_db.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ybond.core_db.database.entities.FavoriteSongEntity
import com.ybond.core_db.database.entities.SavedQueryEntity
import com.ybond.core_db.database.entities.SongOptionsEntity

@Database(
    entities = [
        FavoriteSongEntity::class,
        SongOptionsEntity::class,
        SavedQueryEntity::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favouriteSongsDao(): FavouriteSongsDao
    abstract fun songsSettingsDao(): SongsOptionsDao
    abstract fun savedQueriesDao(): SavedQueriesDao
}