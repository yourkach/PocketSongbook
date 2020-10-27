package com.example.pocketsongbook.common

import android.app.Application
import com.example.pocketsongbook.di.AppComponent
import com.example.pocketsongbook.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class App : Application(), HasAndroidInjector {


    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .application(this)
            .build()

        appComponent.inject(this)
    }

    companion object {
        lateinit var appComponent: AppComponent
        private set
    }
}