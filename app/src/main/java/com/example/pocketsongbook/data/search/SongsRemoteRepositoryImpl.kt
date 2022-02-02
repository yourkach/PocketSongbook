package com.example.pocketsongbook.data.search

import com.example.pocketsongbook.data.search.website_parsers.SongsWebsiteParser
import com.example.pocketsongbook.domain.models.FoundSongModel
import com.example.pocketsongbook.domain.models.SongModel
import com.example.pocketsongbook.domain.search.SongsRemoteRepository
import com.example.pocketsongbook.domain.search.SongsWebsite
import javax.inject.Inject

class SongsRemoteRepositoryImpl @Inject constructor(
    private val parsersMap: Map<SongsWebsite, @JvmSuppressWildcards SongsWebsiteParser>
) : SongsRemoteRepository {

    init {
        require(parsersMap.keys.containsAll(SongsWebsite.values().toList())) {
            "Provide parser for each website"
        }
    }

    private fun getParser(website: SongsWebsite): SongsWebsiteParser {
        return parsersMap[website] ?: throw IllegalStateException("No parser provided")
    }

    override suspend fun loadSearchResults(website: SongsWebsite, query: String): List<FoundSongModel> {
        return getParser(website).loadSearchResults(query)
    }

    override suspend fun loadSong(songSearchItem: FoundSongModel): SongModel {
        return getParser(songSearchItem.website).loadSong(songSearchItem)
    }
}
