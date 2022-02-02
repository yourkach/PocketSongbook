package com.example.pocketsongbook.di

import com.example.pocketsongbook.common.SongbookApplication
import com.example.pocketsongbook.di.modules.ActivityInjectionModule
import com.example.pocketsongbook.di.modules.AppModule
import com.example.pocketsongbook.di.modules.DatabaseModule
import com.example.pocketsongbook.di.modules.DefaultsModule
import com.example.pocketsongbook.di.modules.FavouritesModule
import com.example.pocketsongbook.di.modules.NavigationModule
import com.example.pocketsongbook.di.modules.PrefsBindingModule
import com.example.pocketsongbook.di.modules.SearchModule
import com.example.pocketsongbook.di.modules.SettingsModule
import com.example.pocketsongbook.di.modules.SongModule
import com.example.pocketsongbook.di.modules.TunerModule
import com.example.pocketsongbook.di.modules.WebsiteParsersModule
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
        WebsiteParsersModule::class,
        DatabaseModule::class,
        DefaultsModule::class,
        SettingsModule::class,
        SongModule::class,
        TunerModule::class,
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
            @BindsInstance app: SongbookApplication
        ): AppComponent
    }

}
