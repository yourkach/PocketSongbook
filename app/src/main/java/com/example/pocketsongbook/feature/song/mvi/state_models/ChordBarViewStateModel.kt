package com.example.pocketsongbook.feature.song.mvi.state_models

import com.ybond.core.models.Chord

data class ChordBarViewStateModel(
    val isOpened: Boolean,
    val chords: List<Chord>
)