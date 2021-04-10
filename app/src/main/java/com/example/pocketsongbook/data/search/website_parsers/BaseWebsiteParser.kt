package com.example.pocketsongbook.data.search.website_parsers

import com.example.pocketsongbook.domain.models.FoundSongModel
import com.example.pocketsongbook.domain.models.SongModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

abstract class BaseWebsiteParser : SongsWebsiteParser {

    protected abstract fun buildSearchURL(searchQuery: String): String

    override suspend fun loadSearchResults(searchRequest: String): List<FoundSongModel> {
        return withContext(Dispatchers.IO) {
            val document = Jsoup.connect(buildSearchURL(searchRequest)).get()
            try {
                parseSearchPage(document)
            } catch (e: Throwable) {
                throw ParseSearchPageError(e)
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
        return SongModel.create(foundSong = foundSong, lyrics = lyrics)
    }

    protected abstract fun parseLyricsPage(pageContent: Document): String

}

class ParseSongPageError(override val cause: Throwable? = null) : Throwable()
class ParseSearchPageError(override val cause: Throwable? = null) : Throwable()