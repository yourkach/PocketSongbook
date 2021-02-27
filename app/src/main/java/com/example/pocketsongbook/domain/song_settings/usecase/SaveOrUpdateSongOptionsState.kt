package com.example.pocketsongbook.domain.song_settings.usecase

import com.example.pocketsongbook.domain.song_settings.SongsOptionsRepository
import com.example.pocketsongbook.domain.song_settings.model.SongOptionsState
import javax.inject.Inject

class SaveOrUpdateSongOptionsState @Inject constructor(
    private val songsOptionsRepository: SongsOptionsRepository
) {

    suspend operator fun invoke(songUrl: String, optionsState: SongOptionsState) {
        songsOptionsRepository.saveOptions(songUrl, optionsState)
    }

}