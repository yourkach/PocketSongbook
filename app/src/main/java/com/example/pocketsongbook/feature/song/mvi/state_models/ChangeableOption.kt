package com.example.pocketsongbook.feature.song.mvi.state_models

data class ChangeableOption<T>(
    val selectedValue: T,
    val isDefault: Boolean
)