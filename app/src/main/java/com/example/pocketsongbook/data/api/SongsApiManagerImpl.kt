package com.example.pocketsongbook.data.api

import com.example.pocketsongbook.data.api.websites_api.SongsWebsiteApi
import com.example.pocketsongbook.data.models.Song
import com.example.pocketsongbook.data.models.SongSearchItem

// TODO: 24.08.20 переписать

class SongsApiManagerImpl(vararg songApis: SongsWebsiteApi) :
    SongsApiManager {

    private val songsWebsitesApi: List<SongsWebsiteApi> = songApis.toList()

    override fun getWebsiteNames(): List<String> = songsWebsitesApi.map { it.websiteName }

    private val defaultWebsiteIndex = 0

    override var selectedWebsitePosition = defaultWebsiteIndex
        private set

    override fun switchToWebsite(position: Int): Boolean {
        return when {
            position !in songsWebsitesApi.indices -> {
                throw IndexOutOfBoundsException()
            }
            selectedWebsitePosition != position -> {
                selectedWebsitePosition = position
                true
            }
            else -> false
        }
    }

    override suspend fun getSearchResults(query: String): List<SongSearchItem>? =
        songsWebsitesApi[selectedWebsitePosition].getSearchResults(query)

    override suspend fun getSong(songSearchItem: SongSearchItem): Song? =
        songsWebsitesApi[selectedWebsitePosition].getSong(songSearchItem)
}