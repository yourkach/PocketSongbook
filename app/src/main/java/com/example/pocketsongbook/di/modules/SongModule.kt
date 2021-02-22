package com.example.pocketsongbook.di.modules

import com.example.pocketsongbook.domain.song.SongLyricsFormatter
import com.example.pocketsongbook.domain.song.impl.SongLyricsFormatterImpl
import com.example.pocketsongbook.domain.song.SongTransponder
import com.example.pocketsongbook.domain.song.impl.SongTransponderImpl
import dagger.Binds
import dagger.Module

@Module
interface SongModule {

    @Binds
    fun bindSongTransponder(impl : SongTransponderImpl) : SongTransponder

    @Binds
    fun bindSongFormatter(impl: SongLyricsFormatterImpl) : SongLyricsFormatter

}
