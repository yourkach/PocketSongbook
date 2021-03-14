package com.example.pocketsongbook.domain.search

import com.example.pocketsongbook.domain.models.FoundSongModel
import com.example.pocketsongbook.domain.models.SongModel

interface SongsRemoteRepository {

    suspend fun loadSearchResults(website: SongsWebsite, query: String): List<FoundSongModel>

    suspend fun loadSong(songSearchItem: FoundSongModel): SongModel?

}