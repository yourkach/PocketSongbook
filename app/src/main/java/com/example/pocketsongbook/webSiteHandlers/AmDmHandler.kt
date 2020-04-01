package com.example.pocketsongbook.webSiteHandlers

import com.example.pocketsongbook.data_classes.SongSearchItem
import com.example.pocketsongbook.interfaces.WebSiteHandler
import org.jsoup.nodes.Document

class AmDmHandler : WebSiteHandler {
    private val searchPage = "https://amdm.ru/search/?q="

    override fun makeSearchURL(reqestText: String): String {
        return searchPage + reqestText.replace(' ', '+')
    }

    override fun parseSearchPage(pageContent: Document): ArrayList<SongSearchItem> {
        val elements = pageContent.select("table[class=items]")
            .eq(0)
            .select("td.artist_name")
        val songItems = ArrayList<SongSearchItem>()
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
            songItems.add(SongSearchItem(artist, title, link))
        }
        return songItems
    }

    override fun parseLyricsPage(pageContent: Document): String {
        return pageContent.select("pre[itemprop=chordsBlock]")
            .eq(0).html().toString()
    }
}
