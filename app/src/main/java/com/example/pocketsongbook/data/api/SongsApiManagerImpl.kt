package com.example.pocketsongbook.data.api

import com.example.pocketsongbook.data.api.websites_api.SongsWebsiteApi
import com.example.pocketsongbook.data.models.Song
import com.example.pocketsongbook.data.models.SongSearchItem

class SongsApiManagerImpl(private val songsWebsitesApi: List<SongsWebsiteApi>) :
    SongsApiManager {

    override fun getWebsiteNames(): List<String> = songsWebsitesApi.map { it.websiteName }

    private val defaultRepoIndex = 0

    private var currentRepoIndex = defaultRepoIndex

    override fun switchToWebsite(position: Int): Boolean {
        return when {
            position !in songsWebsitesApi.indices -> {
                throw IndexOutOfBoundsException()
            }
            currentRepoIndex != position -> {
                currentRepoIndex = position
                true
            }
            else -> false
        }
    }

    override suspend fun getSearchResults(query: String): List<SongSearchItem>? =
        songsWebsitesApi[currentRepoIndex].getSearchResults(query)

    override suspend fun getSong(songSearchItem: SongSearchItem): Song? =
        songsWebsitesApi[currentRepoIndex].getSong(songSearchItem)
}