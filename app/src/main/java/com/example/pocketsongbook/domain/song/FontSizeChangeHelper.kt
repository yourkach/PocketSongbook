package com.example.pocketsongbook.domain.song

import com.ybond.core_entities.models.FontSize
import com.ybond.core_entities.models.ChangeType
import com.ybond.core_entities.models.ChangeableOption

interface FontSizeChangeHelper {
    fun changeFontSizeOption(
        currentSize: FontSize,
        changeType: ChangeType
    ): ChangeableOption<FontSize>
}

