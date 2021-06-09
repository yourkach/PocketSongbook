package com.ybond.core.models

data class FoundSongModel(
    val artist: String,
    val title: String,
    val url: String,
    val website: SongsWebsite,
    val isFavourite: Boolean = false
)
