package com.example.pocketsongbook.data.favorites.impl

import com.chibatching.kotpref.KotprefModel
import com.example.pocketsongbook.data.favorites.FavoriteSongsUrlsDao
import javax.inject.Inject

class FavoriteSongsUrlsDaoImpl @Inject constructor() : FavoriteSongsUrlsDao, KotprefModel() {
    override val urls: MutableCollection<String> by stringSetPref()
}