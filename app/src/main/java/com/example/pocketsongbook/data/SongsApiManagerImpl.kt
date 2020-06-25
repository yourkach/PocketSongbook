package com.example.pocketsongbook.data

import com.example.pocketsongbook.data.songs_api.SongsWebsiteApi
import com.example.pocketsongbook.domain.SongsApiManager
import com.example.pocketsongbook.domain.model.Song
import com.example.pocketsongbook.domain.model.SongSearchItem

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