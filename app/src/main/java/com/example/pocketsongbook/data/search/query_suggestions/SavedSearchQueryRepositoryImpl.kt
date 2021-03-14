package com.example.pocketsongbook.data.search.query_suggestions

import com.example.pocketsongbook.data.database.SavedQueriesDao
import com.example.pocketsongbook.domain.search.SavedSearchQueryRepository
import com.example.pocketsongbook.domain.search.suggestions.SavedQueryModel
import javax.inject.Inject

class SavedSearchQueryRepositoryImpl @Inject constructor(
    private val queriesDao: SavedQueriesDao,
    private val modelMapper: QueryModelMapper
): SavedSearchQueryRepository {


    override suspend fun getMatchingQueries(text: String): List<SavedQueryModel> {
        return queriesDao.getMatchingQueries(text).map(modelMapper::toModel)
    }

    override suspend fun saveQuery(queryText: String) {
        val model = SavedQueryModel(queryText,System.currentTimeMillis())
        val entity = modelMapper.toEntity(model)
        queriesDao.insert(entity)
    }
}