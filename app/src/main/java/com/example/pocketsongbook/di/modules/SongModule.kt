package com.example.pocketsongbook.di.modules

import com.example.pocketsongbook.domain.song.*
import com.example.pocketsongbook.domain.song.impl.*
import dagger.Binds
import dagger.Module

@Module
interface SongModule {

    @Binds
    fun bindSongTransponder(impl: SongTransponderImpl): SongTransponder

    @Binds
    fun bindSongFormatter(impl: SongLyricsFormatterImpl): SongLyricsFormatter

    @Binds
    fun bindChordsMapper(impl: ChordsMapperImpl): ChordsMapper

    @Binds
    fun bindFontSizeChangeHelper(impl: FontSizeChangeHelperImpl): FontSizeChangeHelper

    @Binds
    fun bindChordsKeyChangeHelper(impl: ChordsKeyChangeHelperImpl): ChordsKeyChangeHelper

    @Binds
    fun bindDefaultsProvider(impl: DefaultsProviderImpl): DefaultsProvider
}
