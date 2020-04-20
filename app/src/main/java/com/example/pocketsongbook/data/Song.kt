package com.example.pocketsongbook.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Song(val artist: String, val title: String, val lyrics: String, val link: String) :
    Parcelable