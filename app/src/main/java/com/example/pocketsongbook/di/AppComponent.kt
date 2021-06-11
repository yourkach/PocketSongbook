package com.example.pocketsongbook.di

import com.example.pocketsongbook.common.SongbookApplication
import com.example.pocketsongbook.di.modules.*
import com.ybond.data.di.DataComponent
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
        DefaultsModule::class,
        SongModule::class,
        TunerModule::class,
        AndroidInjectionModule::class
    ]
)
interface AppComponent : AndroidInjector<SongbookApplication> {

    @Component.Factory
    interface Builder {
        fun create(
            @BindsInstance app: SongbookApplication
        ): AppComponent
    }

}