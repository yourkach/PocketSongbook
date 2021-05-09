package com.example.pocketsongbook.common.navigation.bottom_navigation

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
enum class NavigationTab : Parcelable {
    Favorites, Search, Tuner
}