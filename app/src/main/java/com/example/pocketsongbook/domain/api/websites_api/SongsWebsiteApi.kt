package com.example.pocketsongbook.domain.api.websites_api

import com.example.pocketsongbook.domain.models.Song
import com.example.pocketsongbook.domain.models.SongSearchItem

interface SongsWebsiteApi {
    val websiteName: String
    suspend fun getSearchResults(searchRequest: String): List<SongSearchItem>?
    suspend fun getSong(songSearchItem: SongSearchItem): Song?
}