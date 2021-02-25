package com.example.pocketsongbook.data.favorites

import com.example.pocketsongbook.domain.models.SongModel

data class FavoriteSongModel(
    val song: SongModel,
    val timeAdded: Long
)