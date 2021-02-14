package com.example.pocketsongbook.domain.impl

import com.example.pocketsongbook.data.network.website_parsers.SongsWebsiteParser
import com.example.pocketsongbook.data.models.SongModel
import com.example.pocketsongbook.data.models.FoundSongModel
import com.example.pocketsongbook.domain.SongsWebsite
import com.example.pocketsongbook.domain.WebSongsRepository
import java.lang.IllegalStateException


class WebSongsRepositoryImpl(vararg websiteParsers: SongsWebsiteParser) :
    WebSongsRepository {

    private val parsersByWebsites: Map<SongsWebsite, SongsWebsiteParser> =
        websiteParsers.associateBy { it.website }

    init {
        if (!parsersByWebsites.keys.containsAll(SongsWebsite.values().toList())) {
            throw IllegalStateException("Provide parser for each website")
        }
    }

    override suspend fun loadSearchResults(website: SongsWebsite, query: String): List<FoundSongModel> {
        return parsersByWebsites[website]?.loadSearchResults(query)
            ?: throw IllegalStateException("No parser provided")
    }

    override suspend fun loadSong(songSearchItem: FoundSongModel): SongModel {
        return parsersByWebsites[songSearchItem.website]?.loadSong(songSearchItem)
            ?: throw IllegalStateException("No parser provided")
    }
}