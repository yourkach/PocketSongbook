package com.example.pocketsongbook.data.search.website_parsers

import com.example.pocketsongbook.domain.models.FoundSongModel
import com.ybond.core.entities.SongsWebsite
import org.jsoup.nodes.Document
import javax.inject.Inject

class AmdmWebsiteParser @Inject constructor() : BaseWebsiteParser() {

    override val website = SongsWebsite.AmDm

    private val baseUrl = "https://amdm.ru/search/?q="

    override fun buildSearchURL(searchQuery: String): String {
        return baseUrl + searchQuery.replace(' ', '+')
    }

    override fun parseSearchPage(pageContent: Document): List<FoundSongModel> {
        val elements = pageContent.select("table[class=items]")
            .eq(0)
            .select("td.artist_name")
        val songItems = mutableListOf<FoundSongModel>()
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
                FoundSongModel(
                    artist = artist,
                    title = title,
                    url = link,
                    website = website
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