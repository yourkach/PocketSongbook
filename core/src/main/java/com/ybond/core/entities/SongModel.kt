package com.ybond.core.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SongModel(
    val artist: String,
    val title: String,
    val lyrics: String,
    val url: String,
    val website: SongsWebsite
) : Parcelable