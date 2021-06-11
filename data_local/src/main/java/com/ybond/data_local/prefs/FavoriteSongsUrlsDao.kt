package com.ybond.data_local.prefs

interface FavoriteSongsUrlsDao {
    val urls: MutableCollection<String>
}

fun FavoriteSongsUrlsDao() : FavoriteSongsUrlsDao = FavoriteSongsUrlsDaoImpl()