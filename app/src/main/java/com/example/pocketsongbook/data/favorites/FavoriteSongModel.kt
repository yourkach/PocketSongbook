package com.example.pocketsongbook.data.favorites

import com.example.pocketsongbook.data.models.SongModel

data class FavoriteSongModel(
    val song: SongModel,
    val timeAdded: Long
)