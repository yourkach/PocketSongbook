package com.ybond.data_local.database.provider

import android.content.Context
import com.ybond.data_local.database.FavouriteSongsDao
import com.ybond.data_local.database.SavedQueriesDao
import com.ybond.data_local.database.SongsOptionsDao
import com.ybond.data_local.prefs.FavoriteSongsUrlsDao

interface DaoProvider {
    fun getFavoritesDao(): FavouriteSongsDao
    fun getSavedQueriesDao(): SavedQueriesDao
    fun getSongsOptionsDao(): SongsOptionsDao
    fun getFavoriteSongUrlsDao(): FavoriteSongsUrlsDao
}

fun DaoProvider(context: Context): DaoProvider = DaoProviderImpl(context)