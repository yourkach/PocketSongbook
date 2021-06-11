package com.ybond.domain.repositories

import com.ybond.core_entities.models.FavoriteSongModel
import com.ybond.core_entities.models.SongModel

interface FavouriteSongsRepository {

    suspend fun getAllFavourites(): List<FavoriteSongModel>

    suspend fun getSongsByQuery(query: String): List<FavoriteSongModel>

    suspend fun findSongByUrl(url: String): FavoriteSongModel?

    suspend fun containsSong(url: String): Boolean

    suspend fun addSong(song: SongModel)

    suspend fun removeSongByUrl(url: String)

}

