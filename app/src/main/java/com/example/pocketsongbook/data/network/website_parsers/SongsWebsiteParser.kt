package com.example.pocketsongbook.data.network.website_parsers

import com.example.pocketsongbook.data.models.SongModel
import com.example.pocketsongbook.data.models.FoundSongModel
import com.example.pocketsongbook.domain.SongsWebsite

interface SongsWebsiteParser {

    val website: SongsWebsite

    suspend fun loadSearchResults(searchRequest: String): List<FoundSongModel>

    suspend fun loadSong(foundSong: FoundSongModel): SongModel

}