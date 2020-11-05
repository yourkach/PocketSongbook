package com.example.pocketsongbook.di.modules

import com.example.pocketsongbook.data.database.FavouriteSongsDao
import com.example.pocketsongbook.data.favourites.FavouriteSongsRepo
import com.example.pocketsongbook.data.favourites.FavouriteSongsRepoImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class FavouritesModule {

    @Provides
    @Singleton
    fun bindFavouritesRepo(favouriteSongsDao: FavouriteSongsDao): FavouriteSongsRepo =
        FavouriteSongsRepoImpl(favouriteSongsDao)

}