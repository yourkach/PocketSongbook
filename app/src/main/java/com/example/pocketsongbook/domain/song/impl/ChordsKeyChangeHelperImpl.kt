package com.example.pocketsongbook.domain.song.impl

import com.example.pocketsongbook.domain.song.ChordsKeyChangeHelper
import com.example.pocketsongbook.domain.song.models.ChordsKey
import com.example.pocketsongbook.feature.song.mvi.state_models.ChangeType
import com.example.pocketsongbook.feature.song.mvi.state_models.ChangeableOption
import javax.inject.Inject

class ChordsKeyChangeHelperImpl @Inject constructor() : ChordsKeyChangeHelper {
    override fun changeChordsKeyOption(
        currentKey: ChordsKey,
        changeType: ChangeType
    ): ChangeableOption<ChordsKey> {
        return when (changeType) {
            ChangeType.Increment -> currentKey.key + 1
            ChangeType.Decrement -> currentKey.key - 1
            ChangeType.SetDefault -> 0
        }.coerceIn(KEY_MIN_VALUE, KEY_MAX_VALUE).let { newKey ->
            ChangeableOption(ChordsKey(newKey), newKey == 0)
        }
    }

    companion object {
        private const val KEY_MIN_VALUE = -6
        private const val KEY_MAX_VALUE = 6
    }
}