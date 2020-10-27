package com.example.pocketsongbook.data.network.websites_api

import com.example.pocketsongbook.data.models.Song
import com.example.pocketsongbook.data.models.SongSearchItem
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException
import javax.inject.Inject

class AmdmWebsiteParser @Inject constructor() : SongsWebsiteParser {

    override val websiteName: String = "AmDm.ru"

    private val baseUrl = "https://amdm.ru/search/?q="

    private fun buildSearchURL(searchQuery: String): String {
        return baseUrl + searchQuery.replace(' ', '+')
    }

    override suspend fun getSearchResults(searchRequest: String): List<SongSearchItem>? {
        return try {
            val document = Jsoup.connect(buildSearchURL(searchRequest)).get()
            parseSearchPage(document)
        } catch (e: IOException) {
            null
        }
    }

    private fun parseSearchPage(pageContent: Document): List<SongSearchItem> {
        val elements = pageContent.select("table[class=items]")
            .eq(0)
            .select("td.artist_name")
        val songItems = mutableListOf<SongSearchItem>()
        elements.forEach { element ->
            val artist = element.select("a[class=artist]")
                .eq(0)
                .text()
            val title = element.select("a[class=artist]")
                .eq(1)
                .text()
            val link = "https:" + element.select("a[class=artist]")
                .eq(1)
                .attr("href")
            songItems.add(
                SongSearchItem(
                    artist,
                    title,
                    link
                )
            )
        }
        return songItems
    }

    override suspend fun getSong(songSearchItem: SongSearchItem): Song? {
        return try {
            val document = Jsoup.connect(songSearchItem.link).get()
            val lyrics = parseLyricsPage(document)
                .replace("\n", "<br>\n")
                .replace(" ", "&nbsp;")
            Song(songSearchItem, lyrics)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    private fun parseLyricsPage(pageContent: Document): String {
        return pageContent.select("pre[itemprop=chordsBlock]")
            .eq(0).html().toString()
    }
}