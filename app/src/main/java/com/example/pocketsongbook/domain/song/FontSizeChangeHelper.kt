package com.example.pocketsongbook.domain.song

import com.example.pocketsongbook.domain.song.models.FontSize
import com.example.pocketsongbook.feature.song.mvi.state_models.ChangeType
import com.example.pocketsongbook.feature.song.mvi.state_models.ChangeableOption

interface FontSizeChangeHelper {
    fun changeFontSizeOption(
        currentSize: FontSize,
        changeType: ChangeType
    ): ChangeableOption<FontSize>
}

