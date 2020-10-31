package com.example.pocketsongbook.data.network.website_parsers

import androidx.core.net.toUri
import com.example.pocketsongbook.data.models.Song
import com.example.pocketsongbook.data.models.SongSearchItem
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.File

abstract class BaseWebsiteParser : SongsWebsiteParser {

    protected abstract fun buildSearchURL(searchQuery: String): String

    override suspend fun getSearchResults(searchRequest: String): List<SongSearchItem> {
        val document = Jsoup.connect(buildSearchURL(searchRequest)).get()
        return parseSearchPage(document)
    }

    protected abstract fun parseSearchPage(pageContent: Document): List<SongSearchItem>

    override suspend fun getSong(songSearchItem: SongSearchItem): Song {
        val document = Jsoup.connect(songSearchItem.link).get()
        val lyrics = parseLyricsPage(document)
            .replace("\n", "<br>\n")
            .replace(" ", "&nbsp;")
        return Song(songSearchItem, lyrics)
    }

    protected abstract fun parseLyricsPage(pageContent: Document): String

}