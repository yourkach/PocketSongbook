package com.example.pocketsongbook.domain.song.impl

import com.example.pocketsongbook.domain.song.FontSizeChangeHelper
import com.example.pocketsongbook.domain.song.defaults.FontChangeDefaults
import com.example.pocketsongbook.domain.song.models.FontSize
import com.example.pocketsongbook.feature.song.mvi.state_models.ChangeType
import com.example.pocketsongbook.feature.song.mvi.state_models.ChangeableOption
import toothpick.InjectConstructor

@InjectConstructor
class FontSizeChangeHelperImpl(
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
