package com.example.pocketsongbook.feature.search.usecase

import com.example.pocketsongbook.common.BaseUseCase
import com.example.pocketsongbook.data.network.WebsitesManager
import com.example.pocketsongbook.data.database.FavouriteSongsDao
import com.example.pocketsongbook.data.models.Song
import com.example.pocketsongbook.data.models.SongSearchItem
import javax.inject.Inject

class GetSongUseCase @Inject constructor(
    private val websitesManager: WebsitesManager,
    private val favouriteSongsDao: FavouriteSongsDao
) : BaseUseCase<SongSearchItem, Song?>() {

    override suspend fun execute(param: SongSearchItem): Song? {
        return favouriteSongsDao.findByUrl(param.url)
            .firstOrNull()?.let { Song(it) }
            ?: websitesManager.getSong(param)
    }
}