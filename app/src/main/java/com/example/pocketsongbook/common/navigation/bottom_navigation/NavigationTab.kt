package com.example.pocketsongbook.common.navigation.bottom_navigation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class NavigationTab : Parcelable {
    Favorites, Search, Tuner
}