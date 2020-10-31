package com.example.pocketsongbook.di.modules

import com.example.pocketsongbook.data.network.WebsitesManager
import com.example.pocketsongbook.data.network.website_parsers.AmdmWebsiteParser
import com.example.pocketsongbook.data.network.website_parsers.MychordsWebsiteParser
import com.example.pocketsongbook.data.network.WebsitesManagerImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class WebsitesModule {

    @Provides
    @Singleton
    fun provideSongsApiManager(amdm : AmdmWebsiteParser, myChords: MychordsWebsiteParser): WebsitesManager =
        WebsitesManagerImpl(amdm, myChords)
}