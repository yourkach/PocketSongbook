package com.example.pocketsongbook.di.modules

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
abstract class AppModule {

    companion object {

        @Provides
        @JvmStatic
        @Singleton
        fun provideContext(application: Application): Context {
            return application.applicationContext
        }

    }
}