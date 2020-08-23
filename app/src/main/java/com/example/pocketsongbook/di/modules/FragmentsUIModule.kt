package com.example.pocketsongbook.di.modules

import com.example.pocketsongbook.di.ActivityScope
import com.example.pocketsongbook.di.FragmentScope
import com.example.pocketsongbook.ui.RootActivity
import com.example.pocketsongbook.ui.fragments.favourites.FavouritesFragment
import com.example.pocketsongbook.ui.fragments.search.SearchFragment
import com.example.pocketsongbook.ui.fragments.song.SongFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentsUIModule {

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
