package com.example.pocketsongbook.di.modules

import com.example.pocketsongbook.data.song_settings.SettingsMapper
import com.example.pocketsongbook.data.song_settings.SettingsMapperImpl
import com.example.pocketsongbook.data.song_settings.SongsOptionsRepositoryImpl
import com.example.pocketsongbook.domain.song_settings.SongsOptionsRepository
import dagger.Binds
import dagger.Module

@Module
interface SettingsModule {

    @Binds
    fun bindSettingsRepository(impl: SongsOptionsRepositoryImpl): SongsOptionsRepository

    @Binds
    fun bindSettingsMapper(impl: SettingsMapperImpl): SettingsMapper

}