package com.example.pocketsongbook.common.navigation.impl

import com.example.pocketsongbook.common.navigation.NavigationTab
import com.example.pocketsongbook.common.navigation.TabCiceronesHolder
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router

class TabCiceronesHolderImpl : TabCiceronesHolder {

    private val cicerones = mutableMapOf<NavigationTab, Cicerone<Router>>()

    override fun getCicerone(tab: NavigationTab): Cicerone<Router> {
        return cicerones[tab] ?: Cicerone.create().also { cicerones[tab] = it }
    }
}