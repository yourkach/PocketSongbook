package com.example.pocketsongbook.domain.song

import com.example.pocketsongbook.domain.song.models.ChordsKey
import com.example.pocketsongbook.feature.song.mvi.state_models.ChangeType
import com.example.pocketsongbook.feature.song.mvi.state_models.ChangeableOption

interface ChordsKeyChangeHelper {
    fun changeChordsKeyOption(
        currentKey: ChordsKey,
        changeType: ChangeType
    ): ChangeableOption<ChordsKey>
}