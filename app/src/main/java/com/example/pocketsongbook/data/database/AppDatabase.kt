package com.example.pocketsongbook.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.pocketsongbook.data.database.entities.FavoriteSongEntity
import com.example.pocketsongbook.data.database.entities.SavedQueryEntity
import com.example.pocketsongbook.data.database.entities.SongOptionsEntity

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
    abstract fun songsOptionsDao(): SongsOptionsDao
    abstract fun savedQueriesDao(): SavedQueriesDao
}