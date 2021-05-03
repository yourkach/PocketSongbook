package com.example.pocketsongbook.data.favorites.impl

import com.chibatching.kotpref.KotprefModel
import com.example.pocketsongbook.data.favorites.FavoriteSongsUrlsDao
import toothpick.InjectConstructor

@InjectConstructor
class FavoriteSongsUrlsDaoImpl : FavoriteSongsUrlsDao, KotprefModel() {
    override val urls: MutableCollection<String> by stringSetPref()
}