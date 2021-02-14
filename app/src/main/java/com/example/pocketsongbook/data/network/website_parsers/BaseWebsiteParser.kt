package com.example.pocketsongbook.data.network.website_parsers

import com.example.pocketsongbook.data.models.SongModel
import com.example.pocketsongbook.data.models.FoundSongModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

abstract class BaseWebsiteParser : SongsWebsiteParser {

    protected abstract fun buildSearchURL(searchQuery: String): String

    override suspend fun loadSearchResults(searchRequest: String): List<FoundSongModel> {
        return withContext(Dispatchers.IO) {
            val document = Jsoup.connect(buildSearchURL(searchRequest)).get()
            parseSearchPage(document)
        }
    }

    protected abstract fun parseSearchPage(pageContent: Document): List<FoundSongModel>

    override suspend fun loadSong(foundSong: FoundSongModel): SongModel {
        val lyrics = withContext(Dispatchers.IO) {
            val document = Jsoup.connect(foundSong.url).get()
            parseLyricsPage(document)
        }
        return SongModel.create(foundSong = foundSong, lyrics = lyrics)
    }

    protected abstract fun parseLyricsPage(pageContent: Document): String

}