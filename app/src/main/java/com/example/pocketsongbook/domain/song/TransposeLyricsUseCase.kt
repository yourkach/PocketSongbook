package com.example.pocketsongbook.domain.song

import com.example.pocketsongbook.domain.song.models.ChordsKey
import com.example.pocketsongbook.domain.song.models.TransposeLyricsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TransposeLyricsUseCase @Inject constructor(
    private val songTransponder: SongTransponder,
    private val lyricsFormatter: SongLyricsFormatter,
    private val chordsMapper: ChordsMapper
) {
    suspend operator fun invoke(rawLyrics: String, transposingKey: ChordsKey): TransposeLyricsResponse {
        return withContext(Dispatchers.Default) {
            val transposingResponse = songTransponder.transpose(rawLyrics, transposingKey)
            val formattedLyrics = lyricsFormatter.formatLyrics(transposingResponse.transposedLyrics)
            val mappedChords = chordsMapper.mapToChords(transposingResponse.transposedChordNames)
            return@withContext TransposeLyricsResponse(
                formattedLyrics,
                mappedChords,
                transposingKey
            )
        }
    }
}

