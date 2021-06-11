package com.ybond.data_network.websites

import com.ybond.core_entities.models.FoundSongModel
import com.ybond.core_entities.models.SongModel
import com.ybond.core_entities.models.SongsWebsite

interface SongsWebsiteDataSource {

    val website: SongsWebsite

    suspend fun loadSearchResults(searchRequest: String): List<FoundSongModel>

    suspend fun loadSong(foundSong: FoundSongModel): SongModel

}

