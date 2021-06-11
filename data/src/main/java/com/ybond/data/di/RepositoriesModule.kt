package com.ybond.data.di

import com.ybond.data.repositories_impl.FavouriteSongsRepositoryImpl
import com.ybond.data.repositories_impl.SavedSearchQueryRepositoryImpl
import com.ybond.data.repositories_impl.SongsOptionsRepositoryImpl
import com.ybond.data.repositories_impl.SongsRemoteRepositoryImpl
import com.ybond.domain.repositories.FavouriteSongsRepository
import com.ybond.domain.repositories.SavedSearchQueryRepository
import com.ybond.domain.repositories.SongsOptionsRepository
import com.ybond.domain.repositories.SongsRemoteRepository
import dagger.Binds
import dagger.Module

@Module
interface RepositoriesModule {

    @Binds
    @DataComponentScope
    fun bindFavoriteSongsRepository(impl: FavouriteSongsRepositoryImpl): FavouriteSongsRepository

    @Binds
    @DataComponentScope
    fun bindSongOptionsRepository(impl: SongsOptionsRepositoryImpl): SongsOptionsRepository

    @Binds
    @DataComponentScope
    fun bindSongsRemoteRepository(impl: SongsRemoteRepositoryImpl): SongsRemoteRepository

    @Binds
    @DataComponentScope
    fun bindSavedSearchQueryRepository(impl: SavedSearchQueryRepositoryImpl): SavedSearchQueryRepository

}