package com.example.pocketsongbook.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SongModel(
    val artist: String,
    val title: String,
    val lyrics: String,
    val url: String
) : Parcelable {

    companion object {
        fun create(foundSong: FoundSongModel, lyrics: String) : SongModel {
            return SongModel(
                artist = foundSong.artist,
                title = foundSong.title,
                lyrics = lyrics,
                url = foundSong.url
            )
        }
    }
}