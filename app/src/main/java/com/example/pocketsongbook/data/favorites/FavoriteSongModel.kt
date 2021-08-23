package com.example.pocketsongbook.data.favorites

import com.ybond.core.entities.SongModel

data class FavoriteSongModel(
    val song: SongModel,
    val timeAdded: Long
)