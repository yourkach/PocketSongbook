package com.example.pocketsongbook.data.network.website_parsers

import com.example.pocketsongbook.data.models.Song
import com.example.pocketsongbook.data.models.SongSearchItem
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException
import javax.inject.Inject

class AmdmWebsiteParser @Inject constructor() : BaseWebsiteParser() {

    override val websiteName: String = "AmDm.ru"

    private val baseUrl = "https://amdm.ru/search/?q="

    override fun buildSearchURL(searchQuery: String): String {
        return baseUrl + searchQuery.replace(' ', '+')
    }

    override fun parseSearchPage(pageContent: Document): List<SongSearchItem> {
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

    override fun parseLyricsPage(pageContent: Document): String {
        return pageContent.select("pre[itemprop=chordsBlock]")
            .eq(0).html().toString()
    }
}