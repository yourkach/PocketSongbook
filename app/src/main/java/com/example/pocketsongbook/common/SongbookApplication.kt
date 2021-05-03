package com.example.pocketsongbook.common

import android.app.Application
import com.example.pocketsongbook.di.annotations.ApplicationScope
import com.example.pocketsongbook.di.modules.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import timber.log.Timber
import toothpick.ktp.KTP
import kotlin.coroutines.CoroutineContext

class SongbookApplication : Application(), CoroutineScope {

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Timber.tag("ApplicationScope").e(throwable, "Error: ")
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob() + exceptionHandler

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        KTP.openScope(ApplicationScope::class.java)
            .installModules(
                AppModule(this),
                DatabaseModule(this),
                SearchSongsModule(),
                FavoritesModule(),
                DefaultsModule(),
                SongOptionsModule()
            )
            .inject(this)
    }

}