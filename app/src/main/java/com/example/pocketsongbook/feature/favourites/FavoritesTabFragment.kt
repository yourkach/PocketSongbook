package com.example.pocketsongbook.feature.favourites

import com.example.pocketsongbook.common.BaseTabContainerFragment
import com.example.pocketsongbook.common.navigation.bottom_navigation.NavigationTab
import com.example.pocketsongbook.common.navigation.toScreen
import com.github.terrakok.cicerone.Screen

class FavoritesTabFragment : BaseTabContainerFragment() {
    override val tab = NavigationTab.Favorites

    override val rootScreen: Screen
        get() = FavouritesFragment().toScreen()
}

