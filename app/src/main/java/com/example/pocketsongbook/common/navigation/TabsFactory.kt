package com.example.pocketsongbook.common.navigation

import com.example.pocketsongbook.common.BaseTabContainerFragment

interface TabsFactory {
    fun createTab(tab: NavigationTab): BaseTabContainerFragment
}