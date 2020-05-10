package com.example.pocketsongbook.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Song(val artist: String, val title: String, val lyrics: String, val link: String) :
    Parcelable {
    constructor(s: SongSearchItem, lyrics: String) : this(s.artist, s.title, lyrics, s.link)
    constructor(s: SongEntity) : this(s.artist, s.title, s.lyrics, s.link)
}