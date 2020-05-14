package com.example.pocketsongbook.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class SongSearchItem(
    val artist: String,
    val title: String,
    val link: String,
    var isFavourite: Boolean = false
)
