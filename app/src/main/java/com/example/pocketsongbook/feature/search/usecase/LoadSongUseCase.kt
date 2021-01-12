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
    private val favouriteSongsRepo: FavouriteSongsRepo
) : BaseUseCase<SongSearchItem, Song?>() {

    override suspend fun execute(param: SongSearchItem): Song? {
        return param.isFavourite
            .takeIf { it }
            ?.let {
                favouriteSongsRepo.findSongByUrl(param.url)?.toSong()
            }
            ?: let {
                websitesManager.loadSong(param)
            }
    }
}