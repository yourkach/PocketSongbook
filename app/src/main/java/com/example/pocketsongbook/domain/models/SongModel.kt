package com.example.pocketsongbook.domain.models

import android.os.Parcelable
import com.example.pocketsongbook.domain.search.SongsWebsite
import kotlinx.parcelize.Parcelize

@Parcelize
data class SongModel(
    val artist: String,
    val title: String,
    val lyrics: String,
    val url: String,
    val website: SongsWebsite
) : Parcelable {

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