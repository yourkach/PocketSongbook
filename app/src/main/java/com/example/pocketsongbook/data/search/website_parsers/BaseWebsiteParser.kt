package com.example.pocketsongbook.data.search.website_parsers

import com.example.pocketsongbook.domain.models.FoundSongModel
import com.ybond.core.entities.SongModel
import com.example.pocketsongbook.domain.models.toSongModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

abstract class BaseWebsiteParser : SongsWebsiteParser {

    protected abstract fun buildSearchURL(searchQuery: String): String

    override suspend fun loadSearchResults(searchRequest: String): List<FoundSongModel> {
        return withContext(Dispatchers.IO) {
            val document = try {
                Jsoup.connect(buildSearchURL(searchRequest)).get()
            } catch (e: Throwable) {
                throw LoadSearchResultsError.ConnectionError(e)
            }
            try {
                parseSearchPage(document)
            } catch (e: Throwable) {
                throw LoadSearchResultsError.ParsingError(e)
            }
        }
    }

    protected abstract fun parseSearchPage(pageContent: Document): List<FoundSongModel>

    override suspend fun loadSong(foundSong: FoundSongModel): SongModel {
        val lyrics = withContext(Dispatchers.IO) {
            val lyricsPageDocument = Jsoup.connect(foundSong.url).get()
            try {
                parseLyricsPage(lyricsPageDocument)
            } catch (e: Throwable) {
                throw ParseSongPageError(e)
            }
        }
        return foundSong.toSongModel(lyrics)
    }

    protected abstract fun parseLyricsPage(pageContent: Document): String

}

class ParseSongPageError(override val cause: Throwable? = null) : Throwable()

sealed class LoadSearchResultsError : Throwable() {
    class ParsingError(override val cause: Throwable? = null) : LoadSearchResultsError()
    class ConnectionError(override val cause: Throwable? = null) : LoadSearchResultsError()
}
