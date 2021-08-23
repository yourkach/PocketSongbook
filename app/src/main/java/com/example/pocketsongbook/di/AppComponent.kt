package com.example.pocketsongbook.di

import com.example.pocketsongbook.common.SongbookApplication
import com.example.pocketsongbook.di.modules.*
import com.ybond.core_db_api.DatabaseApi
import com.ybond.feature_songview_api.SongViewApi
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
        SearchModule::class,
        DatabaseModule::class,
        DefaultsModule::class,
        SettingsModule::class,
        SongModule::class,
        SongViewModule::class,
        TunerModule::class,
        PrefsBindingModule::class,
        FavouritesModule::class,
        AndroidInjectionModule::class,
        AndroidSupportInjectionModule::class,
        ActivityInjectionModule::class
    ]
)
interface AppComponent : AndroidInjector<SongbookApplication>, DatabaseApi, SongViewApi {

    @Component.Factory
    interface Builder {
        fun create(
            @BindsInstance app: SongbookApplication
        ): AppComponent
    }

}