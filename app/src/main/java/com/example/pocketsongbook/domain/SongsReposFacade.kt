package com.example.pocketsongbook.domain

import com.example.pocketsongbook.domain.model.Song
import com.example.pocketsongbook.domain.model.SongSearchItem

interface SongsReposFacade {

    fun getWebsiteNames(): List<String>

    suspend fun getSearchResults(query: String): List<SongSearchItem>?

    suspend fun getSong( songSearchItem: SongSearchItem): Song?

    fun switchToRepo(position: Int): Boolean
}