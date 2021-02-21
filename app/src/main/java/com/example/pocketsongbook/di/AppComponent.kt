package com.example.pocketsongbook.di

import android.app.Application
import com.example.pocketsongbook.common.SongbookApplication
import com.example.pocketsongbook.di.modules.*
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        NavigationModule::class,
        NetworkModule::class,
        RoomModule::class,
        PrefsBindingModule::class,
        FavouritesModule::class,
        AndroidInjectionModule::class,
        AndroidSupportInjectionModule::class,
        ActivityInjectionModule::class
    ]
)
interface AppComponent : AndroidInjector<SongbookApplication> {

    @Component.Factory
    interface Builder {
        fun create(
            @BindsInstance app: Application
        ): AppComponent
    }

}