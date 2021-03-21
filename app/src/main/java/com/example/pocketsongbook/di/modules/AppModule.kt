package com.example.pocketsongbook.di.modules

import android.content.Context
import com.example.pocketsongbook.common.SongbookApplication
import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun provideApplicationScope(application: SongbookApplication): CoroutineScope

    companion object {

        @Provides
        @JvmStatic
        @Singleton
        fun provideContext(application: SongbookApplication): Context {
            return application.applicationContext
        }

    }
}