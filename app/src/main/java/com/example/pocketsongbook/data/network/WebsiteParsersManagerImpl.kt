package com.example.pocketsongbook.data.network

import com.example.pocketsongbook.data.network.website_parsers.SongsWebsiteParser
import com.example.pocketsongbook.data.models.Song
import com.example.pocketsongbook.data.models.SongSearchItem


class WebsiteParsersManagerImpl(vararg websiteParsers: SongsWebsiteParser) :
    WebsiteParsersManager {

    private val websiteParsers: List<SongsWebsiteParser> = websiteParsers.toList()

    override val websiteNames: List<String>
        get() = websiteParsers.map { it.websiteName }

    private var selectedParser = websiteParsers.first()

    override val selectedWebsiteName: String
        get() = selectedParser.websiteName


    override fun selectWebsiteByName(websiteName: String): Boolean {
        return websiteParsers.firstOrNull { parser ->
            parser.websiteName == websiteName
        }.let { parser ->
            when {
                parser != null && parser != selectedParser -> {
                    selectedParser = parser
                    true
                }
                else -> false
            }
        }
    }

    override suspend fun loadSearchResults(query: String): List<SongSearchItem> =
        selectedParser.loadSearchResults(query)

    override suspend fun loadSong(songSearchItem: SongSearchItem): Song =
        selectedParser.loadSong(songSearchItem)
}