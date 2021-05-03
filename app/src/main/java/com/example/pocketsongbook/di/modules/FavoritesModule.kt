package com.example.pocketsongbook.di.modules

import com.example.pocketsongbook.data.favorites.FavoriteSongsUrlsDao
import com.example.pocketsongbook.data.favorites.impl.FavoriteSongsUrlsDaoImpl
import com.example.pocketsongbook.data.favorites.impl.FavouriteSongsRepositoryImpl
import com.example.pocketsongbook.domain.favorites.FavouriteSongsRepository
import toothpick.config.Module

class FavoritesModule : Module() {
    init {
        bind(FavoriteSongsUrlsDao::class.java).to(FavoriteSongsUrlsDaoImpl::class.java)
        bind(FavouriteSongsRepository::class.java).to(FavouriteSongsRepositoryImpl::class.java)
    }
}