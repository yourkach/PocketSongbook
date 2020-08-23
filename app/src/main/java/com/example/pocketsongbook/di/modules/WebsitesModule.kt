package com.example.pocketsongbook.di.modules

import com.example.pocketsongbook.data.api.SongsApiManager
import com.example.pocketsongbook.data.api.websites_api.AmdmWebsiteApi
import com.example.pocketsongbook.data.api.websites_api.MychordsWebsiteApi
import com.example.pocketsongbook.data.api.SongsApiManagerImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class WebsitesModule {

    @Provides
    @Singleton
    fun provideSongsApiManager(amdm : AmdmWebsiteApi, myChords: MychordsWebsiteApi): SongsApiManager =
        SongsApiManagerImpl(amdm, myChords)
}