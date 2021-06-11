package com.ybond.domain.repositories

import com.ybond.core_entities.models.SavedQueryModel

interface SavedSearchQueryRepository {

    suspend fun getMatchingQueries(text: String): List<SavedQueryModel>

    suspend fun saveQuery(queryText: String)

    suspend fun deleteQuery(queryText: String)

}