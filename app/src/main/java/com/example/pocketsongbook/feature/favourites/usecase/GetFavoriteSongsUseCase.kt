package com.example.pocketsongbook.feature.favourites.usecase

import com.ybond.core_entities.models.FavoriteSongModel
import com.ybond.domain.repositories.FavouriteSongsRepository
import com.example.pocketsongbook.feature.favourites.ObtainSongsOption
import javax.inject.Inject

class GetFavoriteSongsUseCase @Inject constructor(
    private val favouriteSongsRepository: com.ybond.domain.repositories.FavouriteSongsRepository
) {

    suspend operator fun invoke(option: ObtainSongsOption): List<FavoriteSongModel> {
        return when (option) {
            is ObtainSongsOption.ByQuery -> {
                favouriteSongsRepository.getSongsByQuery(option.query)
            }
            is ObtainSongsOption.All -> {
                favouriteSongsRepository.getAllFavourites()
            }
        }
            .sortedByDescending {
                it.timeAdded
            }
    }

}
