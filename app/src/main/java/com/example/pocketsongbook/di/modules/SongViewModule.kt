package com.example.pocketsongbook.di.modules

import com.example.pocketsongbook.common.navigation.toScreen
import com.example.pocketsongbook.feature.song.SongFragment
import com.github.terrakok.cicerone.Router
import com.ybond.core.entities.SongModel
import com.ybond.feature_songview_api.SongViewStarter
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
interface SongViewModule {

    companion object {

        @Provides
        @Singleton
        fun provideSongViewStarter() : SongViewStarter = object : SongViewStarter {
            override fun Router.openSongView(songModel: SongModel) {
                navigateTo(SongFragment.newInstance(songModel).toScreen())
            }
        }
    }
}