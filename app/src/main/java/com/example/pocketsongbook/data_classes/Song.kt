package com.example.pocketsongbook.data_classes

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Song(val artist: String, val title: String, val lyrics: String) : Parcelable