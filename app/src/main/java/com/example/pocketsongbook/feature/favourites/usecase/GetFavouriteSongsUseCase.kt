package com.example.pocketsongbook.feature.favourites.usecase

import com.example.pocketsongbook.data.database.entities.SongEntity
import com.example.pocketsongbook.data.favourites.FavouriteSongsRepo
import com.example.pocketsongbook.data.models.SongModel
import javax.inject.Inject

class GetFavouriteSongsUseCase @Inject constructor(
    private val favouriteSongsRepo: FavouriteSongsRepo
) {

    suspend operator fun invoke(param: Param): List<SongModel> {
        return when (param) {
            is Param.ByQuery -> {
                favouriteSongsRepo.getSongsByQuery(param.query)
            }
            is Param.All -> {
                favouriteSongsRepo.getAllFavourites()
            }
        }
            .sortedByDescending {
                it.timeAdded
            }
            .map(SongEntity::toSong)
    }

    sealed class Param {
        data class ByQuery(val query: String) : Param()
        object All : Param()
    }
}
