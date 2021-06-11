package com.ybond.data.repositories_impl

import com.ybond.data_local.database.SavedQueriesDao
import com.ybond.core_entities.models.SavedQueryModel
import com.ybond.data.mappers.QueryModelMapper
import com.ybond.domain.repositories.SavedSearchQueryRepository
import javax.inject.Inject

class SavedSearchQueryRepositoryImpl @Inject constructor(
    private val queriesDao: SavedQueriesDao,
    private val modelMapper: QueryModelMapper
) : SavedSearchQueryRepository {


    override suspend fun getMatchingQueries(text: String): List<SavedQueryModel> {
        return when (text.isNotBlank()) {
            true -> queriesDao.getMatchingQueries(text)
            false -> queriesDao.getAll()
        }
            .map(modelMapper::toModel)
    }

    override suspend fun saveQuery(queryText: String) {
        val model = SavedQueryModel(queryText, System.currentTimeMillis())
        val entity = modelMapper.toEntity(model)
        queriesDao.insert(entity)
    }

    override suspend fun deleteQuery(queryText: String) {
        queriesDao.deleteMatching(queryText)
    }
}