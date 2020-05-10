package com.example.pocketsongbook.di

import android.content.Context
import com.example.pocketsongbook.di.modules.DatabaseModule
import com.example.pocketsongbook.di.modules.WebsitesModule
import com.example.pocketsongbook.ui.activity.FavouritesActivity
import com.example.pocketsongbook.ui.activity.SearchSongActivity
import com.example.pocketsongbook.ui.activity.SongViewActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(
    modules = [
        WebsitesModule::class,
        DatabaseModule::class
    ]
)
@Singleton
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(context: Context): Builder
        fun build(): AppComponent
    }

    fun inject(activity: SearchSongActivity)
    fun inject(activity: FavouritesActivity)
    fun inject(activity: SongViewActivity)
}