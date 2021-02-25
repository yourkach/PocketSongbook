package com.example.pocketsongbook.domain.song.impl

import com.example.pocketsongbook.domain.song.SongTransponder
import com.example.pocketsongbook.domain.song.models.ChordsKey
import com.example.pocketsongbook.utils.ChordsTransponder
import javax.inject.Inject

class SongTransponderImpl @Inject constructor() : SongTransponder {

    override fun transpose(lyrics: String, chordsKey: ChordsKey): SongTransponder.Response {
        val key = chordsKey.key
        return if (key != 0) {
            val chordsSet = parseChords(lyrics)
            val newTransposedChords = mutableListOf<String>()
            val chordsMap = mutableMapOf<String, String>()
            chordsSet.forEach {
                val newChord = ChordsTransponder.transposeChord(
                    it,
                    key
                )
                chordsMap[it] = newChord
                newTransposedChords.add(newChord)
            }
            val newTextBuilder = StringBuilder()
            var chordStartIndex = lyrics.indexOf("<b>")
            var prevChordEnd = 0
            while (chordStartIndex != -1) {
                val chordEnd = lyrics.indexOf("</b>", chordStartIndex + 3)
                val chord =
                    lyrics.substring(
                        chordStartIndex + 3,
                        chordEnd
                    )
                newTextBuilder.apply {
                    append(lyrics.substring(prevChordEnd, chordStartIndex + 3))
                    append(chordsMap[chord])
                    append("</b>")
                }
                prevChordEnd = chordEnd + 4
                chordStartIndex = lyrics.indexOf("<b>", prevChordEnd)
            }
            newTextBuilder.append(lyrics.substring(startIndex = prevChordEnd))
            SongTransponder.Response(
                transposedLyrics = newTextBuilder.toString(),
                transposedChordNames = newTransposedChords
            )
        } else {
            SongTransponder.Response(
                transposedLyrics = lyrics,
                transposedChordNames = parseChords(lyrics).toList()
            )
        }
    }

    private fun parseChords(lyrics: String) : Set<String> {
        return Regex("<b>(.*?)</b>")
            .findAll(lyrics)
            .mapNotNull { it.groups[1]?.value }
            .toSet()
    }
}