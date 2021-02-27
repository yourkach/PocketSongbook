package com.example.pocketsongbook.domain.song_settings.usecase

import com.example.pocketsongbook.domain.models.SongModel
import com.example.pocketsongbook.domain.song_settings.SongsOptionsRepository
import com.example.pocketsongbook.domain.song_settings.model.SongOptionsState
import javax.inject.Inject

class GetSongSettingsUseCase @Inject constructor(
    private val songsOptionsRepository: SongsOptionsRepository,
    private val getDefaultSongSettings: GetDefaultSongSettings
) {

    suspend operator fun invoke(song: SongModel): SongOptionsState {
        return songsOptionsRepository.getSavedOptions(song.url) ?: getDefaultSongSettings()
    }

}

