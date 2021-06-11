package com.ybond.domain.usecases.song_settings

import com.ybond.core_entities.models.FontChangeDefaults
import com.ybond.core_entities.models.KeyChangeDefaults
import com.ybond.core_entities.models.ChordsKey
import com.ybond.core_entities.models.FontSize
import com.ybond.core_entities.models.SongOptionsState
import com.ybond.core_entities.models.ChangeableOption
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