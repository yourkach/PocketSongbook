package com.ybond.data_local.prefs

import com.chibatching.kotpref.KotprefModel

internal class FavoriteSongsUrlsDaoImpl : FavoriteSongsUrlsDao, KotprefModel() {
    override val urls: MutableCollection<String> by stringSetPref()
}