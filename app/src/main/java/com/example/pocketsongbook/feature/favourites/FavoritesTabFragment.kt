package com.example.pocketsongbook.feature.favourites

import com.example.pocketsongbook.common.BaseTabContainerFragment
import com.example.pocketsongbook.common.navigation.toScreen
import com.github.terrakok.cicerone.Screen

class FavoritesTabFragment : BaseTabContainerFragment() {
    override val tabName: String = "FAVORITES"

    override val rootScreen: Screen
        get() = FavouritesFragment().toScreen()
}

