package com.example.pocketsongbook.di.modules

import com.example.pocketsongbook.data.favorites.impl.FavouriteSongsRepositoryImpl
import com.example.pocketsongbook.domain.favorites.FavouriteSongsRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface FavouritesModule {

    @Binds
    @Singleton
    fun bindFavoriteSongsRepository(impl: FavouriteSongsRepositoryImpl): FavouriteSongsRepository

}