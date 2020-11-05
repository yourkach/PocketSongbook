package com.example.pocketsongbook.feature.song.domain

import com.example.pocketsongbook.data.models.Song
import com.example.pocketsongbook.feature.song.ChangeType
import com.example.pocketsongbook.utils.ChordsTransponder
import java.util.regex.Pattern

class SongHolder(
    private val song: Song
) {

    private val chordsSet: Set<String> = parseSongChords()

    var currentState: SongState = evaluateSongState()
        private set(value) {
            field = value
            listener?.onSongStateChanged(field)
        }

    private var chordsKey: Int = 0

    private var listener: SongChangesListener? = null

    fun subscribe(listener: SongChangesListener) {
        this.listener = listener
        listener.onSongStateChanged(currentState)
    }

    private fun parseSongChords(): Set<String> {
        val chordPattern =
            Pattern.compile("<b>(.*?)</b>")
        val matcher = chordPattern.matcher(song.lyrics)
        val chordsFound = mutableListOf<String>()
        while (matcher.find()) {
            val chord = matcher.group(1)
            if (chord != null) chordsFound.add(chord)
        }
        val chordMatches = Regex("<b>(.*?)</b>") // TODO: 05.11.20 потестить работу
            .findAll(song.lyrics)
            .mapNotNull { it.groups[1]?.value }
            .toSet()
        return chordsFound.toSet()
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
                newTextBuilder.append(lyrics.substring(prevChordEnd, chordStartIndex + 3))
                newTextBuilder.append(chordsMap[chord])
                newTextBuilder.append("</b>")
                prevChordEnd = chordEnd + 4
                chordStartIndex = lyrics.indexOf("<b>", prevChordEnd)
            }
            newTextBuilder.append(lyrics.substring(prevChordEnd))
            newTextBuilder.toString() to newTransposedChords
        } else {
            song.lyrics to chordsSet.toList()
        }
    }

    fun changeChordsKey(changeType : ChangeType){
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

    interface SongChangesListener {
        fun onSongStateChanged(newState: SongState)
    }

    data class SongState(
        val formattedSongHtmlText: String,
        val chordsList: List<String>,
        val chordsKey: Int
    )
}

