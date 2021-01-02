package com.example.pocketsongbook.data.network

import com.example.pocketsongbook.data.models.Song
import com.example.pocketsongbook.data.models.SongSearchItem

interface WebsiteParsersManager {

    val selectedWebsiteName: String

    /**
     * returns list of website names
     */
    val websiteNames: List<String>

    /**
     * returns true if switch successful
     */
    fun selectWebsiteByName(websiteName: String): Boolean

    suspend fun loadSearchResults(query: String): List<SongSearchItem>

    suspend fun loadSong(songSearchItem: SongSearchItem): Song?

}