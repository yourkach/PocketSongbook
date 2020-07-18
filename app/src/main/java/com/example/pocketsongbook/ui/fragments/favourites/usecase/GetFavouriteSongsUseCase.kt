package com.example.pocketsongbook.ui.fragments.favourites.usecase

import com.example.pocketsongbook.data.BaseUseCase
import com.example.pocketsongbook.data.database.FavouriteSongsDao
import com.example.pocketsongbook.data.database.SongEntity
import com.example.pocketsongbook.data.models.Song
import javax.inject.Inject

class GetFavouriteSongsUseCase @Inject constructor(
    private val favouriteSongsDao: FavouriteSongsDao
) : BaseUseCase<GetFavouriteSongsUseCase.Param, List<SongEntity>>() {

    override suspend operator fun invoke(param: Param): List<SongEntity> {
        return when (param) {
            is Param.GetByQuery -> {
                favouriteSongsDao.findByName(param.query)
            }
            is Param.GetAll -> {
                favouriteSongsDao.getAll()
            }
        }
    }

    sealed class Param {
        data class GetByQuery(val query: String) : Param()
        object GetAll : Param()
    }
}
