package com.example.pocketsongbook.domain.song.defaults

import javax.inject.Provider

class FontChangeDefaultsProvider : Provider<FontChangeDefaults> {
    override fun get(): FontChangeDefaults {
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
    }
}