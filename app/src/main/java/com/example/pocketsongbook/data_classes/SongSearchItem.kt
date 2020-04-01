package com.example.pocketsongbook.data_classes

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.net.URL

@Parcelize
data class SongSearchItem(val artist: String, val title: String, val link: String) : Parcelable