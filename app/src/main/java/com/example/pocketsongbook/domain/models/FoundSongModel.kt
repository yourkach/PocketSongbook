package com.example.pocketsongbook.domain.models

import com.example.pocketsongbook.domain.search.SongsWebsite

data class FoundSongModel(
    val artist: String,
    val title: String,
    val url: String,
    val website: SongsWebsite,
    val isFavourite: Boolean = false
)
