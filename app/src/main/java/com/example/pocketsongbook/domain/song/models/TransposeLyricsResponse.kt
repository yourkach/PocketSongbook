package com.example.pocketsongbook.domain.song.models

import com.ybond.core_entities.models.Chord
import com.ybond.core_entities.models.ChordsKey

data class TransposeLyricsResponse(
    val formattedLyricsHtml: String,
    val chordsList: List<Chord>,
    val chordsKey: ChordsKey
)