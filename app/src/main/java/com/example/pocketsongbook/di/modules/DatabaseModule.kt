package com.example.pocketsongbook.di.modules

import android.content.Context
import com.ybond.core_db.database.FavouriteSongsDao
import com.ybond.core_db.database.SavedQueriesDao
import com.ybond.core_db.database.SongsOptionsDao
import com.ybond.core_db.database.provider.DaoProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDaoProvider(context: Context): DaoProvider = DaoProvider(context)

    @Provides
    @Singleton
    fun provideFavouriteSongsDao(daoProvider: DaoProvider): FavouriteSongsDao {
        return daoProvider.getFavoritesDao()
    }

    @Provides
    @Singleton
    fun provideQueriesDao(daoProvider: DaoProvider): SavedQueriesDao {
        return daoProvider.getSavedQueriesDao()
    }

    @Provides
    @Singleton
    fun provideSettingsDao(daoProvider: DaoProvider): SongsOptionsDao {
        return daoProvider.getSongsOptionsDao()
    }
}