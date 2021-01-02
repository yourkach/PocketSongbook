package com.example.pocketsongbook.feature.search.usecase

import com.example.pocketsongbook.common.BaseUseCase
import com.example.pocketsongbook.data.network.WebsiteParsersManager
import com.example.pocketsongbook.data.database.FavouriteSongsDao
import com.example.pocketsongbook.data.favourites.FavouriteSongsRepo
import com.example.pocketsongbook.data.models.Song
import com.example.pocketsongbook.data.models.SongSearchItem
import javax.inject.Inject

class LoadSongUseCase @Inject constructor(
    private val websitesManager: WebsiteParsersManager,
    private val favouriteSongsDao: FavouriteSongsDao
) : BaseUseCase<SongSearchItem, Song?>() {

    override suspend fun execute(param: SongSearchItem): Song? {
        return if (param.isFavourite) {
            /** TODO: 02.01.2021 переписать с использованием [FavouriteSongsRepo] */
            favouriteSongsDao.findByUrl(param.url)
                .firstOrNull()
                ?.let { Song(it) }
        } else websitesManager.loadSong(param)
    }
}