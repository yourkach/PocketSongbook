package com.example.pocketsongbook.data.network

import com.example.pocketsongbook.data.models.Song
import com.example.pocketsongbook.data.models.SongSearchItem

interface WebsitesManager {

    val selectedWebsitePosition : Int

    /**
     * returns list of website names
     */
    fun getWebsiteNames(): List<String>

    /**
     * returns true if switch successful
     */
    fun switchToWebsite(position: Int): Boolean

    suspend fun getSearchResults(query: String): List<SongSearchItem>?

    suspend fun getSong( songSearchItem: SongSearchItem): Song?

}