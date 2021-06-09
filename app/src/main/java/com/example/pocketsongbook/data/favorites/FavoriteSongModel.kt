package com.example.pocketsongbook.data.favorites

import com.ybond.core.models.SongModel

data class FavoriteSongModel(
    val song: SongModel,
    val timeAdded: Long
)