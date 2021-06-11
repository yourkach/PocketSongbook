package com.example.pocketsongbook.di

import com.example.pocketsongbook.di.modules.ActivityInjectionModule
import com.ybond.data.di.DataComponent
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.support.AndroidSupportInjectionModule

@Component(
    modules = [
        AndroidInjectionModule::class,
        AndroidSupportInjectionModule::class,
        ActivityInjectionModule::class
    ],
    dependencies = [AppComponent::class, DataComponent::class]
)
@UiComponentScope
interface UiComponent {

    @Component.Factory
    interface Factory {
        fun create(
            appComponent: AppComponent,
            dataComponent: DataComponent
        ): UiComponent
    }

}