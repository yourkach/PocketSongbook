package com.example.pocketsongbook.data.network

import com.example.pocketsongbook.data.network.websites_api.SongsWebsiteParser
import com.example.pocketsongbook.data.models.Song
import com.example.pocketsongbook.data.models.SongSearchItem

// TODO: 24.08.20 переписать

class WebsitesManagerImpl(vararg songParsers: SongsWebsiteParser) :
    WebsitesManager {

    private val songsWebsitesParser: List<SongsWebsiteParser> = songParsers.toList()

    override fun getWebsiteNames(): List<String> = songsWebsitesParser.map { it.websiteName }

    private val defaultWebsiteIndex = 0

    override var selectedWebsitePosition = defaultWebsiteIndex
        private set

    override fun switchToWebsite(position: Int): Boolean {
        return when {
            position !in songsWebsitesParser.indices -> {
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
        songsWebsitesParser[selectedWebsitePosition].getSearchResults(query)

    override suspend fun getSong(songSearchItem: SongSearchItem): Song? =
        songsWebsitesParser[selectedWebsitePosition].getSong(songSearchItem)
}