package com.example.pocketsongbook.domain.song.models

import com.example.pocketsongbook.domain.models.Chord

data class TransposeLyricsResponse(
    val formattedLyricsHtml: String,
    val chordsList: List<Chord>,
    val chordsKey: ChordsKey
)