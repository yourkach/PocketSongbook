package com.example.pocketsongbook.domain.song.impl

import com.ybond.core_entities.models.FontChangeDefaults
import com.example.pocketsongbook.domain.song.FontSizeChangeHelper
import com.ybond.core_entities.models.FontSize
import com.ybond.core_entities.models.ChangeType
import com.ybond.core_entities.models.ChangeableOption
import javax.inject.Inject

class FontSizeChangeHelperImpl @Inject constructor(
    private val defaults: FontChangeDefaults
) : FontSizeChangeHelper {

    override fun changeFontSizeOption(
        currentSize: FontSize,
        changeType: ChangeType
    ): ChangeableOption<FontSize> {
        return when (changeType) {
            ChangeType.Increment -> currentSize.size + defaults.fontSizeChangeAmount
            ChangeType.Decrement -> currentSize.size - defaults.fontSizeChangeAmount
            ChangeType.SetDefault -> defaults.defaultFontSize
        }.coerceIn(defaults.minFontSize, defaults.maxFontSize).let { newSize ->
            ChangeableOption(FontSize(newSize), newSize == defaults.defaultFontSize)
        }
    }
}
