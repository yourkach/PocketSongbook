package com.example.pocketsongbook.data.search.website_parsers

import com.example.pocketsongbook.domain.models.FoundSongModel
import com.ybond.core.entities.SongModel
import com.ybond.core.entities.SongsWebsite

interface SongsWebsiteParser {

    val website: SongsWebsite

    suspend fun loadSearchResults(searchRequest: String): List<FoundSongModel>

    suspend fun loadSong(foundSong: FoundSongModel): SongModel

}