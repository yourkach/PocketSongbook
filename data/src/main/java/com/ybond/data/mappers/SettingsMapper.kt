package com.ybond.data.mappers

import com.ybond.data_local.database.entities.SongOptionsEntity
import com.ybond.core_entities.models.*
import javax.inject.Inject

class SettingsMapper @Inject constructor(
    private val keyChangeDefaults: KeyChangeDefaults,
    private val fontChangeDefaults: FontChangeDefaults
) {
    fun map(entity: SongOptionsEntity): SongOptionsState {
        val fontSize = entity.font_size.coerceIn(
            minimumValue = fontChangeDefaults.minFontSize,
            maximumValue = fontChangeDefaults.maxFontSize
        )
        val chordsKey = entity.chords_key.coerceIn(
            minimumValue = keyChangeDefaults.chordsKeyMin,
            maximumValue = keyChangeDefaults.chordsKeyMax
        )
        val fontOption = ChangeableOption(
            selectedValue = FontSize(fontSize),
            isDefault = fontSize == fontChangeDefaults.defaultFontSize
        )
        val keyOption = ChangeableOption(
            selectedValue = ChordsKey(chordsKey),
            isDefault = chordsKey == keyChangeDefaults.chordsKeyDefault
        )
        return SongOptionsState(
            fontOption = fontOption,
            chordsOption = keyOption
        )
    }

    fun reverseMap(url: String, state: SongOptionsState): SongOptionsEntity {
        return SongOptionsEntity(
            song_url = url,
            chords_key = state.chordsOption.selectedValue.key,
            font_size = state.fontOption.selectedValue.size
        )
    }
}