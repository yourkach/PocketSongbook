package com.example.pocketsongbook.domain.song.defaults

import javax.inject.Provider

class KeyChangeDefaultsProvider : Provider<KeyChangeDefaults> {
    override fun get(): KeyChangeDefaults {
        return KeyChangeDefaults(
            chordsKeyMax = KEY_MAX_VALUE,
            chordsKeyMin = KEY_MIN_VALUE,
            chordsKeyDefault = KEY_DEFAULT_VALUE
        )
    }

    companion object {
        private const val KEY_MIN_VALUE = -6
        private const val KEY_MAX_VALUE = 6
        private const val KEY_DEFAULT_VALUE = 0
    }
}