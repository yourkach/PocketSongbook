package com.example.pocketsongbook.di.modules

import com.example.pocketsongbook.domain.song.defaults.FontChangeDefaults
import com.example.pocketsongbook.domain.song.defaults.FontChangeDefaultsProvider
import com.example.pocketsongbook.domain.song.defaults.KeyChangeDefaults
import com.example.pocketsongbook.domain.song.defaults.KeyChangeDefaultsProvider
import toothpick.config.Module

class DefaultsModule : Module() {
    init {
        bind(KeyChangeDefaults::class.java).toProviderInstance(KeyChangeDefaultsProvider())
        bind(FontChangeDefaults::class.java).toProviderInstance(FontChangeDefaultsProvider())
    }
}

