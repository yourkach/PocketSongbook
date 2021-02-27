package com.example.pocketsongbook.domain.song.impl

import com.example.pocketsongbook.domain.song.DefaultsProvider
import com.example.pocketsongbook.domain.song.FontChangeDefaults
import com.example.pocketsongbook.domain.song.KeyChangeDefaults
import javax.inject.Inject

class DefaultsProviderImpl @Inject constructor() : DefaultsProvider {

    override fun getKeyDefaults(): KeyChangeDefaults {
        return KeyChangeDefaults(
            chordsKeyMax = KEY_MAX_VALUE,
            chordsKeyMin = KEY_MIN_VALUE,
            chordsKeyDefault = KEY_DEFAULT_VALUE
        )
    }

    override fun getFontDefaults(): FontChangeDefaults {
        return FontChangeDefaults(
            minFontSize = FONT_SIZE_MIN,
            maxFontSize = FONT_SIZE_MAX,
            defaultFontSize = FONT_SIZE_DEFAULT,
            fontSizeChangeAmount = FONT_CHANGE_AMOUNT
        )
    }

    companion object {
        private const val FONT_SIZE_DEFAULT: Int = 16
        private const val FONT_SIZE_MIN: Int = 8
        private const val FONT_SIZE_MAX: Int = 36
        private const val FONT_CHANGE_AMOUNT: Int = 2

        private const val KEY_MIN_VALUE = -6
        private const val KEY_MAX_VALUE = 6
        private const val KEY_DEFAULT_VALUE = 0
    }
}