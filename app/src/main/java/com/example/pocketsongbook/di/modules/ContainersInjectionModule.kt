package com.example.pocketsongbook.di.modules

import com.example.pocketsongbook.di.ContainerScope
import com.example.pocketsongbook.feature.favourites.FavoritesTabFragment
import com.example.pocketsongbook.feature.guitar_tuner.TunerTabFragment
import com.example.pocketsongbook.feature.search.SearchTabFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ContainersInjectionModule {


    @ContributesAndroidInjector(modules = [FragmentsInjectionModule::class])
    @ContainerScope
    abstract fun favoritesTabContainer(): FavoritesTabFragment

    @ContributesAndroidInjector(modules = [FragmentsInjectionModule::class])
    @ContainerScope
    abstract fun searchTabContainer(): SearchTabFragment

    @ContributesAndroidInjector(modules = [FragmentsInjectionModule::class])
    @ContainerScope
    abstract fun tunerTabContainer(): TunerTabFragment


}