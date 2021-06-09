package com.example.pocketsongbook.domain.song.models

import com.ybond.core.models.Chord

data class TransposeLyricsResponse(
    val formattedLyricsHtml: String,
    val chordsList: List<Chord>,
    val chordsKey: ChordsKey
)