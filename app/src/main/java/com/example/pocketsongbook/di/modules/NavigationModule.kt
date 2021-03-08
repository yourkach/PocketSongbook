package com.example.pocketsongbook.di.modules

import com.example.pocketsongbook.common.navigation.TabCiceronesHolder
import com.example.pocketsongbook.common.navigation.impl.TabCiceronesHolderImpl
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class NavigationModule {

    @Provides
    @Singleton
    fun provideGlobalCicerone(): Cicerone<Router> = Cicerone.create()

    @Provides
    @Singleton
    fun provideTabCiceronesHolder(): TabCiceronesHolder = TabCiceronesHolderImpl()

}
