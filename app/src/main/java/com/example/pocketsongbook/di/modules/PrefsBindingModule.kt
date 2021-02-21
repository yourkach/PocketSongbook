package com.example.pocketsongbook.di.modules

import com.example.pocketsongbook.data.favorites.FavoriteSongsUrlsDao
import com.example.pocketsongbook.data.favorites.impl.FavoriteSongsUrlsDaoImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface PrefsBindingModule {

    @Binds
    @Singleton
    fun bindFavoriteSongsUrlsDao(daoImpl: FavoriteSongsUrlsDaoImpl): FavoriteSongsUrlsDao

}