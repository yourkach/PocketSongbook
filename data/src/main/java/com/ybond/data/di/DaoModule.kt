package com.ybond.data.di

import android.content.Context
import com.ybond.data_local.database.FavouriteSongsDao
import com.ybond.data_local.database.SavedQueriesDao
import com.ybond.data_local.database.SongsOptionsDao
import com.ybond.data_local.database.provider.DaoProvider
import com.ybond.data_local.prefs.FavoriteSongsUrlsDao
import dagger.Module
import dagger.Provides

@Module
class DaoModule {

    @Provides
    @DataComponentScope
    fun provideDaoProvider(context: Context): DaoProvider = DaoProvider(context)

    @Provides
    @DataComponentScope
    fun provideFavoriteSongsUrlsDao(daoProvider: DaoProvider): FavoriteSongsUrlsDao {
        return daoProvider.getFavoriteSongUrlsDao()
    }

    @Provides
    @DataComponentScope
    fun provideFavoriteSongsDao(daoProvider: DaoProvider): FavouriteSongsDao {
        return daoProvider.getFavoritesDao()
    }

    @Provides
    @DataComponentScope
    fun provideSavedQueriesDao(daoProvider: DaoProvider): SavedQueriesDao {
        return daoProvider.getSavedQueriesDao()
    }

    @Provides
    @DataComponentScope
    fun provideSongOptionsDao(daoProvider: DaoProvider): SongsOptionsDao {
        return daoProvider.getSongsOptionsDao()
    }

}