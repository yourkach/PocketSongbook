package com.example.pocketsongbook.di.modules

import com.example.pocketsongbook.data.song_settings.SettingsMapper
import com.example.pocketsongbook.data.song_settings.SettingsMapperImpl
import com.example.pocketsongbook.data.song_settings.SongsOptionsRepositoryImpl
import com.example.pocketsongbook.domain.song_settings.SongsOptionsRepository
import toothpick.config.Module

class SongOptionsModule : Module() {
    init {
        bind(SettingsMapper::class.java).to(SettingsMapperImpl::class.java)
        bind(SongsOptionsRepository::class.java).to(SongsOptionsRepositoryImpl::class.java)
    }
}

