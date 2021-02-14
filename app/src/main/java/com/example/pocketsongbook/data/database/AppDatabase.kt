package com.example.pocketsongbook.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.pocketsongbook.data.database.entities.SongEntity

@Database(entities = [SongEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favouriteSongsDao() : FavouriteSongsDao
}