package com.example.pocketsongbook.domain.song_settings.model

import com.example.pocketsongbook.domain.song.models.ChordsKey
import com.example.pocketsongbook.domain.song.models.FontSize
import com.example.pocketsongbook.feature.song.mvi.state_models.ChangeableOption

data class SongOptionsState(
    val fontOption: ChangeableOption<FontSize>,
    val chordsOption: ChangeableOption<ChordsKey>
)
