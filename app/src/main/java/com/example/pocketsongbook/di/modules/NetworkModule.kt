package com.example.pocketsongbook.di.modules

import com.example.pocketsongbook.data.network.SongsRemoteRepositoryImpl
import com.example.pocketsongbook.data.network.website_parsers.AmdmWebsiteParser
import com.example.pocketsongbook.data.network.website_parsers.MychordsWebsiteParser
import com.example.pocketsongbook.domain.SongsRemoteRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideWebSongsRepository(
        amdm: AmdmWebsiteParser,
        myChords: MychordsWebsiteParser
    ): SongsRemoteRepository {
        return SongsRemoteRepositoryImpl(amdm, myChords)
    }
}