package com.example.pocketsongbook.domain.song.impl

import com.example.pocketsongbook.domain.song.SongTransponder
import com.example.pocketsongbook.utils.ChordsTransponder
import javax.inject.Inject

class SongTransponderImpl @Inject constructor() : SongTransponder {

    override fun transpose(lyrics: String, newKey: Int): SongTransponder.TransposingResponse {
        return if (newKey != 0) {
            val chordsSet = parseChords(lyrics)
            val newTransposedChords = mutableListOf<String>()
            val chordsMap = mutableMapOf<String, String>()
            chordsSet.forEach {
                val newChord = ChordsTransponder.transposeChord(
                    it,
                    newKey
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
            SongTransponder.TransposingResponse(
                transposedLyrics = newTextBuilder.toString(),
                transposedChords = newTransposedChords
            )
        } else {
            SongTransponder.TransposingResponse(
                transposedLyrics = lyrics,
                transposedChords = parseChords(lyrics).toList()
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