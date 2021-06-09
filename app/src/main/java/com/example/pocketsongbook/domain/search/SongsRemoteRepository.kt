package com.example.pocketsongbook.domain.search

import com.ybond.core.models.FoundSongModel
import com.ybond.core.models.SongModel
import com.ybond.core.models.SongsWebsite

interface SongsRemoteRepository {

    suspend fun loadSearchResults(website: SongsWebsite, query: String): List<FoundSongModel>

    suspend fun loadSong(songSearchItem: FoundSongModel): SongModel

}