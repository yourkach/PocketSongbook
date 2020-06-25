package com.example.pocketsongbook.data.songs_api

import com.example.pocketsongbook.domain.model.Song
import com.example.pocketsongbook.domain.model.SongSearchItem

interface SongsWebsiteApi {
    val websiteName: String
    suspend fun getSearchResults(searchRequest: String): List<SongSearchItem>?
    suspend fun getSong(songSearchItem: SongSearchItem): Song?
}