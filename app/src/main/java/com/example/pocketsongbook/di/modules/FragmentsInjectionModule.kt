package com.example.pocketsongbook.di.modules

import com.example.pocketsongbook.di.FragmentScope
import com.example.pocketsongbook.feature.favourites.FavouritesFragment
import com.example.pocketsongbook.feature.guitar_tuner.permissions_screen.MicroPermissionsFragment
import com.example.pocketsongbook.feature.guitar_tuner.tuner_screen.TunerFragment
import com.example.pocketsongbook.feature.search.SearchFragment
import com.example.pocketsongbook.feature.song.SongFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentsInjectionModule {

    @ContributesAndroidInjector
    @FragmentScope
    abstract fun search(): SearchFragment

    @ContributesAndroidInjector
    @FragmentScope
    abstract fun song(): SongFragment

    @ContributesAndroidInjector
    @FragmentScope
    abstract fun favourites(): FavouritesFragment

    @ContributesAndroidInjector
    @FragmentScope
    abstract fun tuner(): TunerFragment

    @ContributesAndroidInjector
    @FragmentScope
    abstract fun microPermissionFragment(): MicroPermissionsFragment

}
