package com.example.pocketsongbook.domain

import com.example.pocketsongbook.domain.model.Song
import com.example.pocketsongbook.domain.model.SongSearchItem

interface SongsReposManager {

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