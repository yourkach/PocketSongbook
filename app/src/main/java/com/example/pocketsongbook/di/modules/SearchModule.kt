package com.example.pocketsongbook.di.modules

import com.example.pocketsongbook.data.search.SongsRemoteRepositoryImpl
import com.example.pocketsongbook.data.search.query_suggestions.SavedSearchQueryRepositoryImpl
import com.example.pocketsongbook.data.search.website_parsers.AmdmWebsiteParser
import com.example.pocketsongbook.data.search.website_parsers.MychordsWebsiteParser
import com.example.pocketsongbook.domain.search.SavedSearchQueryRepository
import com.example.pocketsongbook.domain.search.SongsRemoteRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
interface SearchModule {


    @Binds
    @Singleton
    fun bindQueriesRepository(impl: SavedSearchQueryRepositoryImpl): SavedSearchQueryRepository

    companion object {
        @Provides
        @Singleton
        fun provideSongsRemoteRepository(
            amdm: AmdmWebsiteParser,
            myChords: MychordsWebsiteParser
        ): SongsRemoteRepository {
            return SongsRemoteRepositoryImpl(amdm, myChords)
        }
    }

}