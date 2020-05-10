package com.example.pocketsongbook.di.modules

import com.example.pocketsongbook.domain.SongsReposFacade
import com.example.pocketsongbook.data.song_repos.AmdmRepo
import com.example.pocketsongbook.data.song_repos.MychordsRepo
import com.example.pocketsongbook.data.SongsReposFacadeImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class WebsitesModule {

    @Provides
    @Singleton
    fun provideSongsRepoFacade(): SongsReposFacade = SongsReposFacadeImpl(
        listOf(
            MychordsRepo(),
            AmdmRepo()
        )
    )
}