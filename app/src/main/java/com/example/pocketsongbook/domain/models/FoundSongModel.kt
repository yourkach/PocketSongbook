package com.example.pocketsongbook.domain.models

import com.example.pocketsongbook.domain.SongsWebsite

data class FoundSongModel(
    val artist: String,
    val title: String,
    val url: String,
    val website: SongsWebsite,
    var isFavourite: Boolean = false
)
