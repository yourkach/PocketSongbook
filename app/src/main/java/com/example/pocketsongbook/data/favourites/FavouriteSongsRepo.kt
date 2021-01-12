package com.example.pocketsongbook.data.favourites

import com.example.pocketsongbook.data.database.SongEntity
import com.example.pocketsongbook.data.models.Song

interface FavouriteSongsRepo {
    suspend fun getAllFavourites(): List<SongEntity>

    suspend fun getSongsByQuery(query: String): List<SongEntity>

    suspend fun findSongByUrl(url: String) : SongEntity?

    suspend fun containsSong(url: String) : Boolean

    suspend fun addSong(song: Song)

    suspend fun removeSong(song: Song)

    suspend fun removeSongByUrl(url: String)
}