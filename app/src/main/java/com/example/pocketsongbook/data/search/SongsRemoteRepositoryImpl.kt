package com.example.pocketsongbook.data.search

import com.example.pocketsongbook.data.search.website_parsers.SongsWebsiteParser
import com.ybond.core.models.FoundSongModel
import com.ybond.core.models.SongModel
import com.example.pocketsongbook.domain.search.SongsRemoteRepository
import com.ybond.core.models.SongsWebsite
class SongsRemoteRepositoryImpl(vararg websiteParsers: SongsWebsiteParser) :
    SongsRemoteRepository {

    private val parsersByWebsites: Map<SongsWebsite, SongsWebsiteParser> =
        websiteParsers.associateBy { it.website }

    init {
        require(parsersByWebsites.keys.containsAll(SongsWebsite.values().toList())) {
            "Provide parser for each website"
        }
    }

    private fun getParser(website: SongsWebsite) : SongsWebsiteParser {
        return parsersByWebsites[website] ?: throw IllegalStateException("No parser provided")
    }

    override suspend fun loadSearchResults(website: SongsWebsite, query: String): List<FoundSongModel> {
        return getParser(website).loadSearchResults(query)
    }

    override suspend fun loadSong(songSearchItem: FoundSongModel): SongModel {
        return getParser(songSearchItem.website).loadSong(songSearchItem)
    }
}