package com.example.pocketsongbook.feature.song.mvi.state_models

import com.ybond.core_entities.models.ChangeType

sealed class SongScreenInteractionEvent {

    data class ChangeFontSize(
        val changeType: ChangeType
    ) : SongScreenInteractionEvent()

    data class ChangeChordsKey(
        val changeType: ChangeType
    ) : SongScreenInteractionEvent()

    data class FavoriteStatusChanged(
        val isFavorite: Boolean
    ) : SongScreenInteractionEvent()

    object ChordsBarButtonClick : SongScreenInteractionEvent()

}