package com.example.pocketsongbook.domain.models

data class SongSearchItem(
    val artist: String,
    val title: String,
    val link: String,
    var isFavourite: Boolean = false
)
