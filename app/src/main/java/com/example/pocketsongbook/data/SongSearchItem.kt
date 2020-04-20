package com.example.pocketsongbook.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SongSearchItem(val artist: String, val title: String, val link: String) : Parcelable