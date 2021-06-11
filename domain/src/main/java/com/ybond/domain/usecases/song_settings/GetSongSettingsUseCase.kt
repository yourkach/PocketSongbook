package com.ybond.domain.usecases.song_settings

import com.ybond.core_entities.models.SongModel
import com.ybond.core_entities.models.SongOptionsState
import javax.inject.Inject

class GetSongSettingsUseCase @Inject constructor(
    private val songsOptionsRepository: com.ybond.domain.repositories.SongsOptionsRepository,
    private val getDefaultSongSettings: GetDefaultSongSettings
) {

    suspend operator fun invoke(song: SongModel): SongOptionsState {
        return songsOptionsRepository.getSavedOptions(song.url) ?: getDefaultSongSettings()
    }

}

