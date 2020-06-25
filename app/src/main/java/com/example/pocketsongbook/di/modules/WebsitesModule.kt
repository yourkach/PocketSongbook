package com.example.pocketsongbook.di.modules

import com.example.pocketsongbook.domain.SongsApiManager
import com.example.pocketsongbook.data.songs_api.AmdmWebsiteApi
import com.example.pocketsongbook.data.songs_api.MychordsWebsiteApi
import com.example.pocketsongbook.data.SongsApiManagerImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class WebsitesModule {

    @Provides
    @Singleton
    fun provideSongsRepoManager(): SongsApiManager = SongsApiManagerImpl(
        listOf(
            MychordsWebsiteApi(),
            AmdmWebsiteApi()
        )
    )
}