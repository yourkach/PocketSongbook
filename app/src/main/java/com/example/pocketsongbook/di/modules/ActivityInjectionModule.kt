package com.example.pocketsongbook.di.modules

import com.example.pocketsongbook.di.ActivityScope
import com.example.pocketsongbook.common.RootActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityInjectionModule {

    @ContributesAndroidInjector(modules = [FragmentsInjectionModule::class])
    @ActivityScope
    abstract fun rootActivity(): RootActivity

}