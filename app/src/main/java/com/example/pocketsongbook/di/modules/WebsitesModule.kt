package com.example.pocketsongbook.di.modules

import com.example.pocketsongbook.domain.SongsReposManager
import com.example.pocketsongbook.data.song_repos.AmdmRepo
import com.example.pocketsongbook.data.song_repos.MychordsRepo
import com.example.pocketsongbook.data.SongsReposManagerImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class WebsitesModule {

    @Provides
    @Singleton
    fun provideSongsRepoManager(): SongsReposManager = SongsReposManagerImpl(
        listOf(
            MychordsRepo(),
            AmdmRepo()
        )
    )
}