package com.example.pocketsongbook.di

import dagger.Module
import dagger.Provides
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import javax.inject.Singleton

@Module
class NavigationModule {

    @Provides
    @Singleton
    fun provideGlobalCicerone(): Cicerone<Router> = Cicerone.create()

    @Provides
    @Singleton
    fun provideGlobalRouter(cicerone: Cicerone<Router>) : Router = cicerone.router

}
