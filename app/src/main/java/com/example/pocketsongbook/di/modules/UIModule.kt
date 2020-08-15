package com.example.pocketsongbook.di.modules

import com.example.pocketsongbook.ui.fragments.song.SongFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class UIModule {

    @ContributesAndroidInjector
    abstract fun songFragment(): SongFragment

}