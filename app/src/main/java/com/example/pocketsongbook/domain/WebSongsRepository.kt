package com.example.pocketsongbook.domain

import com.example.pocketsongbook.data.models.SongModel
import com.example.pocketsongbook.data.models.FoundSongModel

interface WebSongsRepository {

    suspend fun loadSearchResults(website: SongsWebsite, query: String): List<FoundSongModel>

    suspend fun loadSong(songSearchItem: FoundSongModel): SongModel?

}