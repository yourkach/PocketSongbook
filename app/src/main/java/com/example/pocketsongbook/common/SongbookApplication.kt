package com.example.pocketsongbook.common

import com.example.pocketsongbook.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

class SongbookApplication : DaggerApplication(), CoroutineScope  {

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Timber.tag("ApplicationScope").e(throwable,"Error: ")
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob() + exceptionHandler

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
        DaggerAppComponent.factory().create(this)

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }

}