package com.example.pocketsongbook.data.favourites

import com.example.pocketsongbook.data.database.entities.SongEntity
import com.example.pocketsongbook.data.models.SongModel

interface FavouriteSongsRepository {
    suspend fun getAllFavourites(): List<SongEntity>

    suspend fun getSongsByQuery(query: String): List<SongEntity>

    suspend fun findSongByUrl(url: String) : SongEntity?

    suspend fun containsSong(url: String) : Boolean

    suspend fun addSong(song: SongModel)

    suspend fun removeSong(song: SongModel)

    suspend fun removeSongByUrl(url: String)
}