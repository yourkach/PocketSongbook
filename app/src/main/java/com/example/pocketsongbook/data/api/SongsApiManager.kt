package com.example.pocketsongbook.data.api

import com.example.pocketsongbook.data.models.Song
import com.example.pocketsongbook.data.models.SongSearchItem

interface SongsApiManager {

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