package com.example.pocketsongbook.data.models

import android.os.Parcelable
import com.example.pocketsongbook.data.database.SongEntity
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Song(val artist: String, val title: String, val lyrics: String, val url: String) :
    Parcelable {
    constructor(s: SongSearchItem, lyrics: String) : this(s.artist, s.title, lyrics, s.url)
    constructor(s: SongEntity) : this(s.artist, s.title, s.lyrics, s.url)
}