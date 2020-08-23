package com.example.pocketsongbook.di

import android.app.Application
import android.content.Context
import com.example.pocketsongbook.App
import com.example.pocketsongbook.di.modules.*
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        NavigationModule::class,
        WebsitesModule::class,
        RoomModule::class,
        AssistantInjectModule::class,
        AndroidInjectionModule::class,
        AndroidSupportInjectionModule::class,
        MainUIModule::class
    ]
)
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(app : Application) : Builder
        fun build(): AppComponent
    }

    val appContext: Context

    fun inject(app: App)

}