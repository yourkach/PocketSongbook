package com.example.pocketsongbook.di.modules

import com.example.pocketsongbook.data.search.website_parsers.AmdmWebsiteParser
import com.example.pocketsongbook.data.search.website_parsers.MychordsWebsiteParser
import com.example.pocketsongbook.data.search.website_parsers.SongsWebsiteParser
import com.example.pocketsongbook.di.WebsiteKey
import com.example.pocketsongbook.domain.search.SongsWebsite
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module
interface WebsiteParsersModule {

    @IntoMap
    @WebsiteKey(SongsWebsite.AmDm)
    @Binds
    @Singleton
    fun provideAmdmParser(impl: AmdmWebsiteParser): SongsWebsiteParser

    @IntoMap
    @WebsiteKey(SongsWebsite.MyChords)
    @Binds
    @Singleton
    fun provideMychordsParser(impl: MychordsWebsiteParser): SongsWebsiteParser

}
