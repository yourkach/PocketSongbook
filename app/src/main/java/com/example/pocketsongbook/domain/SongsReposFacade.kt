package com.example.pocketsongbook.domain

import com.example.pocketsongbook.domain.model.Song
import com.example.pocketsongbook.domain.model.SongSearchItem

interface SongsReposFacade {

    fun getWebsiteNames(): List<String>

    fun switchToWebsite(position: Int): Boolean

    suspend fun getSearchResults(query: String): List<SongSearchItem>?

    suspend fun getSong( songSearchItem: SongSearchItem): Song?

}