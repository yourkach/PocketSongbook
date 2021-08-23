package com.example.pocketsongbook.domain.models

import com.ybond.core.entities.SongsWebsite
import com.ybond.core.entities.SongModel

data class FoundSongModel(
    val artist: String,
    val title: String,
    val url: String,
    val website: SongsWebsite,
    val isFavourite: Boolean = false
)

fun FoundSongModel.toSongModel(lyrics: String): SongModel = SongModel(
    artist = artist,
    title = title,
    lyrics = lyrics,
    url = url,
    website = website
)