package com.example.pocketsongbook.di

import android.content.Context
import com.example.pocketsongbook.di.modules.DatabaseModule
import com.example.pocketsongbook.di.modules.WebsitesModule
import com.example.pocketsongbook.ui.fragments.FavouritesFragment
import com.example.pocketsongbook.ui.fragments.SearchFragment
import com.example.pocketsongbook.ui.fragments.SongFragment
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
/*
    fun inject(activity: SearchSongActivity)
    fun inject(activity: FavouritesActivity)
    fun inject(activity: SongViewActivity)
*/

    fun inject(fragment: SearchFragment)
    fun inject(fragment: FavouritesFragment)
    fun inject(fragment: SongFragment)

}