package com.ybond.core.models

data class SongModel(
    val artist: String,
    val title: String,
    val lyrics: String,
    val url: String,
    val website: SongsWebsite
) {

    companion object {
        fun create(foundSong: FoundSongModel, lyrics: String) : SongModel {
            return SongModel(
                artist = foundSong.artist,
                title = foundSong.title,
                lyrics = lyrics,
                url = foundSong.url,
                foundSong.website
            )
        }
    }
}