package com.example.pocketsongbook.data.network.website_parsers

import com.example.pocketsongbook.data.models.Song
import com.example.pocketsongbook.data.models.SongSearchItem

interface SongsWebsiteParser {

    val websiteName: String

    suspend fun loadSearchResults(searchRequest: String): List<SongSearchItem>

    suspend fun loadSong(songSearchItem: SongSearchItem): Song

}