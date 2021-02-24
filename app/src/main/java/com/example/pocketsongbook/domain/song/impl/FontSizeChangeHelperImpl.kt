package com.example.pocketsongbook.domain.song.impl

import com.example.pocketsongbook.domain.song.FontSizeChangeHelper
import com.example.pocketsongbook.domain.song.models.FontSize
import com.example.pocketsongbook.feature.song.mvi.state_models.ChangeType
import com.example.pocketsongbook.feature.song.mvi.state_models.ChangeableOption
import javax.inject.Inject

class FontSizeChangeHelperImpl @Inject constructor() : FontSizeChangeHelper {

    override fun changeFontSizeOption(
        currentSize: FontSize,
        changeType: ChangeType
    ): ChangeableOption<FontSize> {
        return when (changeType) {
            ChangeType.Increment -> currentSize.size + FONT_CHANGE_AMOUNT
            ChangeType.Decrement -> currentSize.size - FONT_CHANGE_AMOUNT
            ChangeType.SetDefault -> FONT_SIZE_DEFAULT
        }.coerceIn(FONT_SIZE_MIN, FONT_SIZE_MAX).let { newSize ->
            ChangeableOption(FontSize(newSize), newSize == FONT_SIZE_DEFAULT)
        }
    }

    companion object {
        private const val FONT_SIZE_DEFAULT: Int = 16
        private const val FONT_SIZE_MIN: Int = 8
        private const val FONT_SIZE_MAX: Int = 36
        private const val FONT_CHANGE_AMOUNT: Int = 2
    }
}
