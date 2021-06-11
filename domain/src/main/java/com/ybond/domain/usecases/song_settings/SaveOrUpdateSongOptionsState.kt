package com.ybond.domain.usecases.song_settings

import com.ybond.domain.repositories.SongsOptionsRepository
import com.ybond.core_entities.models.SongOptionsState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class SaveOrUpdateSongOptionsState @Inject constructor(
    private val songsOptionsRepository: com.ybond.domain.repositories.SongsOptionsRepository,
    private val applicationScope: CoroutineScope
) {

    suspend operator fun invoke(songUrl: String, optionsState: SongOptionsState) {
        applicationScope.launch {
            songsOptionsRepository.saveOptions(songUrl, optionsState)
        }.join()
    }

}