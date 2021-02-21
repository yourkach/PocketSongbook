package com.example.pocketsongbook.common.navigation.impl

import com.example.pocketsongbook.common.navigation.TabCiceronesHolder
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router

class TabCiceronesHolderImpl : TabCiceronesHolder {

    private val cicerones = mutableMapOf<String, Cicerone<Router>>()

    override fun getCicerone(tabName: String): Cicerone<Router> {
        return cicerones[tabName] ?: Cicerone.create().also { cicerones[tabName] = it }
    }
}