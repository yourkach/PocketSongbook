package com.example.pocketsongbook.di.modules

import com.example.pocketsongbook.data.database.FavouriteSongsDao
import com.example.pocketsongbook.data.favorites.FavoriteSongsUrlsDao
import com.example.pocketsongbook.data.favorites.impl.FavouriteSongsRepositoryImpl
import com.example.pocketsongbook.domain.favorites.FavouriteSongsRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class FavouritesModule {

    @Provides
    @Singleton
    fun provideFavouritesRepo(
        songsDao: FavouriteSongsDao,
        urlsDao: FavoriteSongsUrlsDao
    ): FavouriteSongsRepository {
        return FavouriteSongsRepositoryImpl(songsDao, urlsDao)
    }

}