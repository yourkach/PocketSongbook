package com.ybond.core_db.database.provider

import android.content.Context
import androidx.room.Room
import com.ybond.core_db.database.AppDatabase
import com.ybond.core_db.database.FavouriteSongsDao
import com.ybond.core_db.database.SavedQueriesDao
import com.ybond.core_db.database.SongsOptionsDao

internal class DaoProviderImpl(private val context: Context) : DaoProvider {
    private val database by lazy {
        Room.databaseBuilder(context, AppDatabase::class.java, "songbook_database").build()
    }

    override fun getFavoritesDao(): FavouriteSongsDao = database.favouriteSongsDao()

    override fun getSavedQueriesDao(): SavedQueriesDao = database.savedQueriesDao()

    override fun getSongsOptionsDao(): SongsOptionsDao = database.songsSettingsDao()

}

interface DaoProvider {
    fun getFavoritesDao(): FavouriteSongsDao
    fun getSavedQueriesDao(): SavedQueriesDao
    fun getSongsOptionsDao(): SongsOptionsDao

    companion object {
        operator fun invoke(context: Context): DaoProvider = DaoProviderImpl(context)
    }
}