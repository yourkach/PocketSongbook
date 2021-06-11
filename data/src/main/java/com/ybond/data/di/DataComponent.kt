package com.ybond.data.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component


@Component(
    modules = [
        RepositoriesModule::class,
        DataSourcesModule::class,
        DaoModule::class
    ]
)
@DataComponentScope
interface DataComponent {

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context
        ): DataComponent
    }

}

