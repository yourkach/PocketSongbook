package com.example.pocketsongbook

import android.app.Application
import com.example.pocketsongbook.di.AppComponent
import com.example.pocketsongbook.di.DaggerAppComponent

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .context(this)
            .build()
    }

    companion object {
        lateinit var appComponent: AppComponent
    }
}