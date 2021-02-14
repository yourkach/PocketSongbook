package com.example.pocketsongbook.common

import android.app.Application
import com.example.pocketsongbook.di.AppComponent
import com.example.pocketsongbook.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class SongbookApplication : DaggerApplication()  {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
        DaggerAppComponent.factory().create(this)

}