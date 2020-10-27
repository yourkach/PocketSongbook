package com.example.pocketsongbook.data.network.websites_api

import com.example.pocketsongbook.data.models.Song
import com.example.pocketsongbook.data.models.SongSearchItem

interface SongsWebsiteParser {
    val websiteName: String
    suspend fun getSearchResults(searchRequest: String): List<SongSearchItem>?
    suspend fun getSong(songSearchItem: SongSearchItem): Song?
}