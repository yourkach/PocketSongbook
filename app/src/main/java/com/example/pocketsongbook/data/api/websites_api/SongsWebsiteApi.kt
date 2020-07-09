package com.example.pocketsongbook.data.api.websites_api

import com.example.pocketsongbook.data.models.Song
import com.example.pocketsongbook.data.models.SongSearchItem

interface SongsWebsiteApi {
    val websiteName: String
    suspend fun getSearchResults(searchRequest: String): List<SongSearchItem>?
    suspend fun getSong(songSearchItem: SongSearchItem): Song?
}