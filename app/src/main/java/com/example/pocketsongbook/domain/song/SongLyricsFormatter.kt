package com.example.pocketsongbook.domain.song

interface SongLyricsFormatter {
    fun formatLyrics(lyrics: String): String
}
