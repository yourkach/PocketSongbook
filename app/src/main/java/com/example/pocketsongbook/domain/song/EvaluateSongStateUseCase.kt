package com.example.pocketsongbook.domain.song

import com.example.pocketsongbook.data.models.SongModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EvaluateSongStateUseCase @Inject constructor(
    private val songTransponder: SongTransponder,
    private val lyricsFormatter: SongLyricsFormatter
) {

    suspend operator fun invoke(song: SongModel, transposingKey: Int = 0): SongViewState {
        return withContext(Dispatchers.Default) {
            val transposingResponse = songTransponder.transpose(song.lyrics, transposingKey)
            val formattedLyrics = lyricsFormatter.formatLyrics(transposingResponse.transposedLyrics)
            return@withContext SongViewState(
                formattedLyrics,
                transposingResponse.transposedChords,
                transposingKey
            )
        }
    }


}

data class SongViewState(
    val formattedLyricsText: String,
    val chordsList: List<String>,
    val chordsKey: Int
)

