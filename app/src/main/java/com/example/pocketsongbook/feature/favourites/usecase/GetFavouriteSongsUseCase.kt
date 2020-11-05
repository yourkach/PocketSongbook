package com.example.pocketsongbook.feature.favourites.usecase

import com.example.pocketsongbook.common.BaseUseCase
import com.example.pocketsongbook.data.database.FavouriteSongsDao
import com.example.pocketsongbook.data.database.SongEntity
import com.example.pocketsongbook.data.favourites.FavouriteSongsRepo
import com.example.pocketsongbook.data.models.Song
import javax.inject.Inject

class GetFavouriteSongsUseCase @Inject constructor(
    private val favouriteSongsRepo: FavouriteSongsRepo
) : BaseUseCase<GetFavouriteSongsUseCase.Param, List<Song>>() {

    override suspend fun execute(param: Param): List<Song> {
        return when (param) {
            is Param.ByQuery -> {
                favouriteSongsRepo.getSongsByQuery(param.query)
            }
            is Param.All -> {
                favouriteSongsRepo.getAllFavourites()
            }
        }
    }

    sealed class Param {
        data class ByQuery(val query: String) : Param()
        object All : Param()
    }
}
