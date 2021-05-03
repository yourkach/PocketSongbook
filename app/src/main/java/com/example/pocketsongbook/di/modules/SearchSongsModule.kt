package com.example.pocketsongbook.di.modules

import com.example.pocketsongbook.data.search.ParsersProvider
import com.example.pocketsongbook.data.search.ParsersProviderImpl
import com.example.pocketsongbook.data.search.SongsRemoteRepositoryImpl
import com.example.pocketsongbook.data.search.query_suggestions.SavedSearchQueryRepositoryImpl
import com.example.pocketsongbook.domain.search.SavedSearchQueryRepository
import com.example.pocketsongbook.domain.search.SongsRemoteRepository
import toothpick.config.Module

class SearchSongsModule : Module() {
    init {
        bind(ParsersProvider::class.java).to(ParsersProviderImpl::class.java)
        bind(SongsRemoteRepository::class.java).to(SongsRemoteRepositoryImpl::class.java)
        bind(SavedSearchQueryRepository::class.java).to(SavedSearchQueryRepositoryImpl::class.java)
    }
}

