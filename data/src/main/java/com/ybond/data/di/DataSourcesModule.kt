package com.ybond.data.di

import com.ybond.data_network.websites.WebsiteDataSourcesHolder
import dagger.Module
import dagger.Provides

@Module
class DataSourcesModule {

    @Provides
    @DataComponentScope
    fun provideWebsiteDataSourcesHolder() : WebsiteDataSourcesHolder = WebsiteDataSourcesHolder()

}