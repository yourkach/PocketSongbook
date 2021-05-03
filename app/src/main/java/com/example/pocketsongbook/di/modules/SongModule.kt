package com.example.pocketsongbook.di.modules

import com.example.pocketsongbook.domain.song.*
import com.example.pocketsongbook.domain.song.impl.*
import toothpick.config.Module

class SongModule : Module() {
    init {
        bind(SongTransponder::class.java).to(SongTransponderImpl::class.java)
        bind(SongLyricsFormatter::class.java).to(SongLyricsFormatterImpl::class.java)
        bind(ChordsMapper::class.java).to(ChordsMapperImpl::class.java)
        bind(FontSizeChangeHelper::class.java).to(FontSizeChangeHelperImpl::class.java)
        bind(ChordsKeyChangeHelper::class.java).to(ChordsKeyChangeHelperImpl::class.java)
    }
}