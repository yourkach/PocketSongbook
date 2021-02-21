package com.example.pocketsongbook.feature.search

import com.example.pocketsongbook.common.BaseTabContainerFragment
import com.example.pocketsongbook.common.navigation.toScreen
import com.github.terrakok.cicerone.Screen

class SearchTabFragment : BaseTabContainerFragment() {
    override val tabName: String = "SEARCH"

    override val rootScreen: Screen
        get() = SearchFragment().toScreen()
}