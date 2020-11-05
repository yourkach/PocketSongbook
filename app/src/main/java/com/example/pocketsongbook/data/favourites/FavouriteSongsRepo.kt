package com.example.pocketsongbook.data.favourites

import com.example.pocketsongbook.data.models.Song

interface FavouriteSongsRepo {
    suspend fun getAllFavourites(): List<Song>

    suspend fun getSongsByQuery(query: String): List<Song>

    suspend fun containsSong(url: String) : Boolean

    suspend fun addSong(song: Song)

    suspend fun removeSong(song: Song)
}