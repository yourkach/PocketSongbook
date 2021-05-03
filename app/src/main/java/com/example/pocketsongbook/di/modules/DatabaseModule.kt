package com.example.pocketsongbook.di.modules

import androidx.room.Room
import com.example.pocketsongbook.common.SongbookApplication
import com.example.pocketsongbook.data.database.AppDatabase
import com.example.pocketsongbook.data.database.FavouriteSongsDao
import com.example.pocketsongbook.data.database.SavedQueriesDao
import com.example.pocketsongbook.data.database.SongsOptionsDao
import toothpick.config.Module
import toothpick.ktp.binding.bind

class DatabaseModule(application: SongbookApplication) : Module() {

    init {
        val database = Room.databaseBuilder(
            application,
            AppDatabase::class.java,
            "songbook_database"
        ).build()
        bind<FavouriteSongsDao>().toInstance(database.favouriteSongsDao())
        bind<SavedQueriesDao>().toInstance(database.savedQueriesDao())
        bind<SongsOptionsDao>().toInstance(database.songsOptionsDao())
    }

}