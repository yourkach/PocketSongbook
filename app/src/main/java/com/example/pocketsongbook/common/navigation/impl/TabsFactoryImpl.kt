package com.example.pocketsongbook.common.navigation.impl

import com.example.pocketsongbook.common.BaseTabContainerFragment
import com.example.pocketsongbook.common.navigation.bottom_navigation.NavigationTab
import com.example.pocketsongbook.common.navigation.bottom_navigation.TabsFactory
import com.example.pocketsongbook.feature.favourites.FavoritesTabFragment
import com.example.pocketsongbook.feature.guitar_tuner.TunerTabFragment
import com.example.pocketsongbook.feature.search.SearchTabFragment

class TabsFactoryImpl : TabsFactory {
    override fun createTab(tab: NavigationTab): BaseTabContainerFragment {
        return when (tab) {
            NavigationTab.Favorites -> FavoritesTabFragment()
            NavigationTab.Search -> SearchTabFragment()
            NavigationTab.Tuner -> TunerTabFragment()
        }
    }
}