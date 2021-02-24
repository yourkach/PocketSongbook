package com.example.pocketsongbook.data.network.website_parsers

import com.example.pocketsongbook.domain.SongsWebsite
import com.example.pocketsongbook.domain.models.FoundSongModel
import com.example.pocketsongbook.domain.models.SongModel

interface SongsWebsiteParser {

    val website: SongsWebsite

    suspend fun loadSearchResults(searchRequest: String): List<FoundSongModel>

    suspend fun loadSong(foundSong: FoundSongModel): SongModel

}