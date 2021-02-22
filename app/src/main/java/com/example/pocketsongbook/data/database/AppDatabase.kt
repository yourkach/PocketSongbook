package com.example.pocketsongbook.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.pocketsongbook.data.database.entities.FavoriteSongEntity
import com.example.pocketsongbook.data.database.entities.SongSettingsEntity

@Database(entities = [FavoriteSongEntity::class, SongSettingsEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favouriteSongsDao(): FavouriteSongsDao
    abstract fun songsSettingsDao(): SongsSettingsDao
}