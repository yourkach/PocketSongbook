package com.example.pocketsongbook.domain.song

interface SongTransponder {
    fun transpose(lyrics: String, key: Int): TransposingResponse

    data class TransposingResponse(
        val transposedLyrics: String,
        val transposedChords: List<String>
    )
}