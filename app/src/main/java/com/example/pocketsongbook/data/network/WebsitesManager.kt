package com.example.pocketsongbook.data.network

import com.example.pocketsongbook.data.models.Song
import com.example.pocketsongbook.data.models.SongSearchItem

interface WebsitesManager {

    val selectedWebsiteName: String

    /**
     * returns list of website names
     */
    fun getWebsiteNames(): List<String>

    /**
     * returns true if switch successful
     */
    fun selectByName(websiteName: String): Boolean

    suspend fun getSearchResults(query: String): List<SongSearchItem>

    suspend fun getSong(songSearchItem: SongSearchItem): Song?

}