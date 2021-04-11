package com.example.pocketsongbook.domain.search

import com.example.pocketsongbook.domain.search.suggestions.SavedQueryModel

interface SavedSearchQueryRepository {

    suspend fun getMatchingQueries(text: String): List<SavedQueryModel>

    suspend fun saveQuery(queryText: String)

    suspend fun deleteQuery(queryText: String)

}