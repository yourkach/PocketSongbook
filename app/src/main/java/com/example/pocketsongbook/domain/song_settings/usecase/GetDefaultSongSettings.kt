package com.example.pocketsongbook.domain.song_settings.usecase

import com.example.pocketsongbook.domain.song.FontChangeDefaults
import com.example.pocketsongbook.domain.song.KeyChangeDefaults
import com.example.pocketsongbook.domain.song.models.ChordsKey
import com.example.pocketsongbook.domain.song.models.FontSize
import com.example.pocketsongbook.domain.song_settings.model.SongOptionsState
import com.example.pocketsongbook.feature.song.mvi.state_models.ChangeableOption
import javax.inject.Inject

class GetDefaultSongSettings @Inject constructor(
    private val keyChangeDefaults: KeyChangeDefaults,
    private val fontChangeDefaults: FontChangeDefaults
) {

    operator fun invoke(): SongOptionsState {
        val font = FontSize(fontChangeDefaults.defaultFontSize)
        val key = ChordsKey(keyChangeDefaults.chordsKeyDefault)
        return SongOptionsState(
            fontOption = ChangeableOption(font, true),
            chordsOption = ChangeableOption(key, true),
        )
    }

}