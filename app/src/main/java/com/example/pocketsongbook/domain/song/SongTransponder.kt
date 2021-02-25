package com.example.pocketsongbook.domain.song

import com.example.pocketsongbook.domain.song.models.ChordsKey

interface SongTransponder {
    fun transpose(lyrics: String, chordsKey: ChordsKey): Response

    data class Response(
        val transposedLyrics: String,
        val transposedChordNames: List<String>
    )
}