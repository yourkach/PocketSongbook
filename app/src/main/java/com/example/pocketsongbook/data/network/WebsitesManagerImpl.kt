package com.example.pocketsongbook.data.network

import com.example.pocketsongbook.data.network.website_parsers.SongsWebsiteParser
import com.example.pocketsongbook.data.models.Song
import com.example.pocketsongbook.data.models.SongSearchItem


class WebsitesManagerImpl(vararg websiteParsers: SongsWebsiteParser) :
    WebsitesManager {

    private val websiteParsers: List<SongsWebsiteParser> = websiteParsers.toList()

    override fun getWebsiteNames(): List<String> = websiteParsers.map { it.websiteName }

    private var currentParser = websiteParsers.first()

    override val selectedWebsiteName: String
        get() = currentParser.websiteName


    override fun selectByName(websiteName: String): Boolean {
        return kotlin.runCatching {
            currentParser = websiteParsers.first { it.websiteName == websiteName }
        }.isSuccess
    }

    override suspend fun getSearchResults(query: String): List<SongSearchItem> =
        currentParser.getSearchResults(query)

    override suspend fun getSong(songSearchItem: SongSearchItem): Song =
        currentParser.getSong(songSearchItem)
}