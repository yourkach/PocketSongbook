package com.example.pocketsongbook.domain.song_settings.usecase

import com.example.pocketsongbook.domain.song_settings.SongsOptionsRepository
import com.example.pocketsongbook.domain.song_settings.model.SongOptionsState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class SaveOrUpdateSongOptionsState @Inject constructor(
    private val songsOptionsRepository: SongsOptionsRepository,
    private val applicationScope: CoroutineScope
) {

    suspend operator fun invoke(songUrl: String, optionsState: SongOptionsState) {
        applicationScope.launch {
            songsOptionsRepository.saveOptions(songUrl, optionsState)
        }.join()
    }

}