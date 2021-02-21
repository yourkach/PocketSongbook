package com.example.pocketsongbook.di

import com.example.pocketsongbook.common.navigation.TabCiceronesHolder
import com.example.pocketsongbook.common.navigation.impl.TabCiceronesHolderImpl
import dagger.Module
import dagger.Provides
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
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
