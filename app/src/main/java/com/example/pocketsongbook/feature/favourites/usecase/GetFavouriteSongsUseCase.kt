package com.example.pocketsongbook.feature.favourites.usecase

import com.example.pocketsongbook.common.BaseUseCase
import com.example.pocketsongbook.data.database.FavouriteSongsDao
import com.example.pocketsongbook.data.database.SongEntity
import javax.inject.Inject

class GetFavouriteSongsUseCase @Inject constructor(
    private val favouriteSongsDao: FavouriteSongsDao
) : BaseUseCase<GetFavouriteSongsUseCase.Param, List<SongEntity>>() {

    override suspend fun execute(param: Param): List<SongEntity> {
        return when (param) {
            is Param.ByQuery -> {
                favouriteSongsDao.findByName(param.query)
            }
            is Param.All -> {
                favouriteSongsDao.getAll()
            }
        }
    }

    sealed class Param {
        data class ByQuery(val query: String) : Param()
        object All : Param()
    }
}
