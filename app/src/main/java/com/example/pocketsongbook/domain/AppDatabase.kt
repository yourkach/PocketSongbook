package com.example.pocketsongbook.domain

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.pocketsongbook.domain.model.SongEntity

@Database(entities = [SongEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favouriteSongsDao() : FavouriteSongsDao
}