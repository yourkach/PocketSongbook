package com.example.pocketsongbook.data.search.query_suggestions

import com.example.pocketsongbook.data.database.entities.SavedQueryEntity
import com.example.pocketsongbook.domain.search.suggestions.SavedQueryModel
import toothpick.InjectConstructor

@InjectConstructor
class QueryModelMapper {

    fun toModel(entity: SavedQueryEntity): SavedQueryModel {
        return SavedQueryModel(text = entity.text, savedAt = entity.saved_at)
    }

    fun toEntity(model: SavedQueryModel): SavedQueryEntity {
        return SavedQueryEntity(text = model.text, saved_at = model.savedAt)
    }

}