package com.example.pocketsongbook.di.modules

import com.example.pocketsongbook.common.RootActivity
import com.example.pocketsongbook.di.ActivityScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityInjectionModule {

    @ContributesAndroidInjector(modules = [ContainersInjectionModule::class])
    @ActivityScope
    abstract fun rootActivity(): RootActivity

}

