package com.example.pocketsongbook.di.modules

import com.example.pocketsongbook.data.database.FavouriteSongsDao
import com.example.pocketsongbook.data.favourites.FavouriteSongsRepository
import com.example.pocketsongbook.data.favourites.FavouriteSongsRepositoryImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class FavouritesModule {

    @Provides
    @Singleton
    fun bindFavouritesRepo(favouriteSongsDao: FavouriteSongsDao): FavouriteSongsRepository =
        FavouriteSongsRepositoryImpl(favouriteSongsDao)

}