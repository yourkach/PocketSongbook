package com.ybond.data.mappers

import com.ybond.data_local.database.entities.SavedQueryEntity
import com.ybond.core_entities.models.SavedQueryModel
import javax.inject.Inject

class QueryModelMapper @Inject constructor() {

    fun toModel(entity: SavedQueryEntity): SavedQueryModel {
        return SavedQueryModel(text = entity.text, savedAt = entity.saved_at)
    }

    fun toEntity(model: SavedQueryModel): SavedQueryEntity {
        return SavedQueryEntity(
            text = model.text,
            saved_at = model.savedAt
        )
    }

}