package com.example.pocketsongbook.domain.favorites

import com.example.pocketsongbook.data.favorites.FavoriteSongModel
import com.example.pocketsongbook.domain.models.SongModel

interface FavouriteSongsRepository {
    suspend fun getAllFavourites(): List<FavoriteSongModel>

    suspend fun getSongsByQuery(query: String): List<FavoriteSongModel>

    suspend fun findSongByUrl(url: String): FavoriteSongModel?

    suspend fun containsSong(url: String): Boolean

    suspend fun addSong(song: SongModel)

    suspend fun removeSongByUrl(url: String)
}

