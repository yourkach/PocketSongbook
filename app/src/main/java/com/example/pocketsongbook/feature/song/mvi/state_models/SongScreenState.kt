package com.example.pocketsongbook.feature.song.mvi.state_models

data class SongScreenState(
    val songState: SongViewStateModel,
    val chordsBarState: ChordBarViewStateModel
)