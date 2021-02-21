package com.example.pocketsongbook.domain

import com.example.pocketsongbook.data.models.FoundSongModel
import com.example.pocketsongbook.data.models.SongModel

interface SongsRemoteRepository {

    suspend fun loadSearchResults(website: SongsWebsite, query: String): List<FoundSongModel>

    suspend fun loadSong(songSearchItem: FoundSongModel): SongModel?

}