package com.ybond.data_network.websites

import com.ybond.core_entities.models.FoundSongModel
import com.ybond.core_entities.models.InternalError
import com.ybond.core_entities.models.SongModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

abstract class BaseWebsiteDataSource : SongsWebsiteDataSource {

    protected abstract fun buildSearchURL(searchQuery: String): String

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun loadSearchResults(searchRequest: String): List<FoundSongModel> {
        return withContext(Dispatchers.IO) {
            val document = try {
                Jsoup.connect(buildSearchURL(searchRequest)).get()
            } catch (e: Throwable) {
                throw InternalError.LoadSearchResultsError.ConnectionError(e)
            }
            try {
                parseSearchPage(document)
            } catch (e: Throwable) {
                throw InternalError.LoadSearchResultsError.ParsingError(e)
            }
        }
    }

    protected abstract fun parseSearchPage(pageContent: Document): List<FoundSongModel>

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun loadSong(foundSong: FoundSongModel): SongModel {
        val lyrics = withContext(Dispatchers.IO) {
            val lyricsPageDocument = Jsoup.connect(foundSong.url).get()
            try {
                parseLyricsPage(lyricsPageDocument)
            } catch (e: Throwable) {
                throw InternalError.ParseSongPageError(e)
            }
        }
        return SongModel.create(foundSong = foundSong, lyrics = lyrics)
    }

    protected abstract fun parseLyricsPage(pageContent: Document): String

}

