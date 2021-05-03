package com.example.pocketsongbook.di.modules

import com.example.pocketsongbook.common.navigation.TabCiceronesHolder
import com.example.pocketsongbook.common.navigation.impl.TabCiceronesHolderImpl
import com.example.pocketsongbook.utils.GlobalCiceroneHolder
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import toothpick.config.Module

class NavigationModule : Module() {
    private val ciceroneHolder = object : GlobalCiceroneHolder {
        override val globalCicerone: Cicerone<Router> = Cicerone.create()
    }

    init {
        bind(GlobalCiceroneHolder::class.java).toInstance(ciceroneHolder)
        bind(TabCiceronesHolder::class.java).to(TabCiceronesHolderImpl::class.java)
    }
}
