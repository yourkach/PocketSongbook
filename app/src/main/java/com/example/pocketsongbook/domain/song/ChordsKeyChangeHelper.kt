package com.example.pocketsongbook.domain.song

import com.ybond.core_entities.models.ChordsKey
import com.ybond.core_entities.models.ChangeType
import com.ybond.core_entities.models.ChangeableOption

interface ChordsKeyChangeHelper {
    fun changeChordsKeyOption(
        currentKey: ChordsKey,
        changeType: ChangeType
    ): ChangeableOption<ChordsKey>
}