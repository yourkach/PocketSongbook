package com.example.pocketsongbook.domain.song.impl

import com.example.pocketsongbook.domain.song.ChordsKeyChangeHelper
import com.example.pocketsongbook.domain.song.KeyChangeDefaults
import com.example.pocketsongbook.domain.song.models.ChordsKey
import com.example.pocketsongbook.feature.song.mvi.state_models.ChangeType
import com.example.pocketsongbook.feature.song.mvi.state_models.ChangeableOption
import javax.inject.Inject

class ChordsKeyChangeHelperImpl @Inject constructor(
    private val defaults: KeyChangeDefaults
) : ChordsKeyChangeHelper {
    override fun changeChordsKeyOption(
        currentKey: ChordsKey,
        changeType: ChangeType
    ): ChangeableOption<ChordsKey> {
        return when (changeType) {
            ChangeType.Increment -> currentKey.key + 1
            ChangeType.Decrement -> currentKey.key - 1
            ChangeType.SetDefault -> 0
        }.coerceIn(defaults.chordsKeyMin, defaults.chordsKeyMax).let { newKey ->
            ChangeableOption(ChordsKey(newKey), newKey == defaults.chordsKeyDefault)
        }
    }
}