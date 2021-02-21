package com.example.pocketsongbook.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.pocketsongbook.data.database.entities.FavoriteSongEntity

@Database(entities = [FavoriteSongEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favouriteSongsDao() : FavouriteSongsDao
}