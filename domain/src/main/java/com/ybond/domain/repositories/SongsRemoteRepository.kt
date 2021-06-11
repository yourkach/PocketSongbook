package com.ybond.domain.repositories

import com.ybond.core_entities.models.FoundSongModel
import com.ybond.core_entities.models.SongModel
import com.ybond.core_entities.models.SongsWebsite

interface SongsRemoteRepository {

    suspend fun loadSearchResults(website: SongsWebsite, query: String): List<FoundSongModel>

    suspend fun loadSong(songSearchItem: FoundSongModel): SongModel

}