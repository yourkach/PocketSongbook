package com.example.pocketsongbook.feature.song.domain

import com.example.pocketsongbook.data.models.SongModel
import com.example.pocketsongbook.feature.song.ChangeType
import com.example.pocketsongbook.utils.ChordsTransponder

// TODO: 20.02.2021 отрефакторить, такой холдер с подписками - бредовое решение
class SongHolder(
    private val song: SongModel,
    private val onSongStateChanged: (SongState) -> Unit
) {

    private val chordsSet: Set<String> = parseSongChords()

    var currentState: SongState = evaluateSongState()
        private set(value) {
            field = value
            onSongStateChanged(value)
        }

    private var chordsKey: Int = 0

    init {
        onSongStateChanged(currentState)
    }

    private fun parseSongChords(): Set<String> {
        return Regex("<b>(.*?)</b>")
            .findAll(song.lyrics)
            .mapNotNull { it.groups[1]?.value }
            .toSet()
    }

    private fun getLyricsWithChords(keyValue: Int): Pair<String, List<String>> {
        return if (keyValue != 0) {
            val newTransposedChords = mutableListOf<String>()
            val chordsMap = mutableMapOf<String, String>()
            chordsSet.forEach {
                val newChord = ChordsTransponder.transposeChord(
                    it,
                    keyValue
                )
                chordsMap[it] = newChord
                newTransposedChords.add(newChord)
            }
            val lyrics = song.lyrics
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
            newTextBuilder.toString() to newTransposedChords
        } else {
            song.lyrics to chordsSet.toList()
        }
    }

    fun changeChordsKey(changeType: ChangeType) {
        chordsKey = when (changeType) {
            ChangeType.Increment -> (chordsKey + 1) % 12
            ChangeType.Decrement -> (chordsKey - 1) % 12
            ChangeType.SetDefault -> 0
        }
        currentState = evaluateSongState()
    }

    private fun evaluateSongState(): SongState {
        val (transposedLyrics, transposedChords) = getLyricsWithChords(chordsKey)
        return SongState(
            transposedLyrics
                .replace("\n", "<br>\n")
                .replace(" ", "&nbsp;"),
            transposedChords,
            chordsKey
        )
    }

    data class SongState(
        val formattedHtmlLyricsText: String,
        val chordsList: List<String>,
        val chordsKey: Int
    )
}

