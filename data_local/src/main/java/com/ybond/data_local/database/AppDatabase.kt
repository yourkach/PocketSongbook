package com.ybond.data_local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ybond.data_local.database.entities.FavoriteSongEntity
import com.ybond.data_local.database.entities.SavedQueryEntity
import com.ybond.data_local.database.entities.SongOptionsEntity

@Database(
    entities = [
        FavoriteSongEntity::class,
        SongOptionsEntity::class,
        SavedQueryEntity::class
    ],
    version = 1
)
internal abstract class AppDatabase : RoomDatabase() {
    abstract fun favouriteSongsDao(): FavouriteSongsDao
    abstract fun songsSettingsDao(): SongsOptionsDao
    abstract fun savedQueriesDao(): SavedQueriesDao
}