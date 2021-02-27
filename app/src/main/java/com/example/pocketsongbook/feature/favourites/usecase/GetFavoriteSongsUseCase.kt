package com.example.pocketsongbook.feature.favourites.usecase

import com.example.pocketsongbook.data.favorites.FavoriteSongModel
import com.example.pocketsongbook.domain.favorites.FavouriteSongsRepository
import com.example.pocketsongbook.feature.favourites.ObtainSongsOption
import javax.inject.Inject

class GetFavoriteSongsUseCase @Inject constructor(
    private val favouriteSongsRepository: FavouriteSongsRepository
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
