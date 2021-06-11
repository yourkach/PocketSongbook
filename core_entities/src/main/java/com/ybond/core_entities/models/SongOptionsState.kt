package com.ybond.core_entities.models

data class SongOptionsState(
    val fontOption: ChangeableOption<FontSize>,
    val chordsOption: ChangeableOption<ChordsKey>
)
