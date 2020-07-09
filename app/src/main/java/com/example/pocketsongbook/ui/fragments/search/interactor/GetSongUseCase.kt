package com.example.pocketsongbook.ui.fragments.search.interactor

import com.example.pocketsongbook.data.BaseUseCase
import com.example.pocketsongbook.data.api.SongsApiManager
import com.example.pocketsongbook.data.database.FavouriteSongsDao
import com.example.pocketsongbook.data.models.Song
import com.example.pocketsongbook.data.models.SongSearchItem
import javax.inject.Inject

class GetSongUseCase @Inject constructor(
    private val songsApiManager: SongsApiManager,
    private val favouriteSongsDao: FavouriteSongsDao
) : BaseUseCase<SongSearchItem, Song?>() {

    override suspend operator fun invoke(param: SongSearchItem): Song? {
        return favouriteSongsDao.findByUrl(param.link)
            .firstOrNull()?.let { Song(it) }
            ?: songsApiManager.getSong(param)
    }
}