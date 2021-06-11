package com.ybond.data_local.database.provider

import android.content.Context
import androidx.room.Room
import com.ybond.data_local.database.AppDatabase
import com.ybond.data_local.database.FavouriteSongsDao
import com.ybond.data_local.database.SavedQueriesDao
import com.ybond.data_local.database.SongsOptionsDao
import com.ybond.data_local.prefs.FavoriteSongsUrlsDao
import com.ybond.data_local.prefs.FavoriteSongsUrlsDaoImpl

internal class DaoProviderImpl(private val context: Context) : DaoProvider {
    private val database by lazy {
        Room.databaseBuilder(context, AppDatabase::class.java, "songbook_database").build()
    }

    override fun getFavoritesDao(): FavouriteSongsDao = database.favouriteSongsDao()

    override fun getSavedQueriesDao(): SavedQueriesDao = database.savedQueriesDao()

    override fun getSongsOptionsDao(): SongsOptionsDao = database.songsSettingsDao()

    override fun getFavoriteSongUrlsDao(): FavoriteSongsUrlsDao = FavoriteSongsUrlsDaoImpl()

}

