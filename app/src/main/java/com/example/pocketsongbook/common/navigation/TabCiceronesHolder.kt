package com.example.pocketsongbook.common.navigation

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router

interface TabCiceronesHolder {
    fun getCicerone(tab: NavigationTab): Cicerone<Router>
}