package com.example.pocketsongbook.data.models

data class SongSearchItem(
    val artist: String,
    val title: String,
    val url: String,
    var isFavourite: Boolean = false
)
