package com.example.pocketsongbook.di.modules

import com.example.pocketsongbook.di.FragmentScope
import com.example.pocketsongbook.feature.favourites.FavouritesFragment
import com.example.pocketsongbook.feature.search.SearchFragment
import com.example.pocketsongbook.feature.song.SongFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentsModule {

    @ContributesAndroidInjector
    @FragmentScope
    abstract fun search(): SearchFragment


    @ContributesAndroidInjector
    @FragmentScope
    abstract fun song(): SongFragment

    @ContributesAndroidInjector
    @FragmentScope
    abstract fun favourites(): FavouritesFragment

}
