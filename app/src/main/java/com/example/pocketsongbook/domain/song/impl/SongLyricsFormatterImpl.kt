package com.example.pocketsongbook.domain.song.impl

import com.example.pocketsongbook.domain.song.SongLyricsFormatter
import toothpick.InjectConstructor

@InjectConstructor
class SongLyricsFormatterImpl : SongLyricsFormatter {
    override fun formatLyrics(lyrics: String): String {
        return lyrics
            .replace("\n", "<br>\n")
            .replace(" ", "&nbsp;")
    }
}