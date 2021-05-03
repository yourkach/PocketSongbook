package com.example.pocketsongbook.di.modules

import android.content.Context
import com.example.pocketsongbook.common.SongbookApplication
import kotlinx.coroutines.CoroutineScope
import toothpick.config.Module

class AppModule(private val application: SongbookApplication) : Module() {

    init {
        bind(Context::class.java).toInstance(application)
        bind(CoroutineScope::class.java).toInstance(application)
    }

}