package com.example.pocketsongbook.data.song_settings

import com.example.pocketsongbook.domain.song.FontChangeDefaults
import com.example.pocketsongbook.domain.song.KeyChangeDefaults
import com.example.pocketsongbook.domain.song.models.ChordsKey
import com.example.pocketsongbook.domain.song.models.FontSize
import com.example.pocketsongbook.domain.song_settings.model.SongOptionsState
import com.example.pocketsongbook.feature.song.mvi.state_models.ChangeableOption
import com.ybond.core_db_api.entities.SongOptionsEntity
import javax.inject.Inject

class SettingsMapperImpl @Inject constructor(
    private val keyChangeDefaults: KeyChangeDefaults,
    private val fontChangeDefaults: FontChangeDefaults
) : SettingsMapper {
    override fun map(entity: SongOptionsEntity): SongOptionsState {
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

    override fun reverseMap(url: String, state: SongOptionsState): SongOptionsEntity {
        return SongOptionsEntity(
            song_url = url,
            chords_key = state.chordsOption.selectedValue.key,
            font_size = state.fontOption.selectedValue.size
        )
    }
}