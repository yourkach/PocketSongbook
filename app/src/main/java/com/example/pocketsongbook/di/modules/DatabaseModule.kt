package com.example.pocketsongbook.di.modules

import android.content.Context
import androidx.room.Room
import com.example.pocketsongbook.data.database.AppDatabase
import com.ybond.core_db_api.dao.FavoriteSongsDao
import com.ybond.core_db_api.dao.SavedQueriesDao
import com.ybond.core_db_api.dao.SongsOptionsDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(context: Context): AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java, "songbook_database"
    ).build()

    @Provides
    @Singleton
    fun provideFavoriteSongsDao(database: AppDatabase): FavoriteSongsDao =
        database.favoriteSongsDao()

    @Provides
    @Singleton
    fun provideQueriesDao(database: AppDatabase): SavedQueriesDao =
        database.savedQueriesDao()

    @Provides
    @Singleton
    fun provideSettingsDao(database: AppDatabase): SongsOptionsDao =
        database.songsOptionsDao()
}