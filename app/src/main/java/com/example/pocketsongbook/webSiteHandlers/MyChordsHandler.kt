package com.example.pocketsongbook.webSiteHandlers

import com.example.pocketsongbook.data_classes.SongSearchItem
import com.example.pocketsongbook.interfaces.WebSiteHandler
import org.jsoup.nodes.Document

class MyChordsHandler : WebSiteHandler {
    private val searchPage = "https://mychords.net/search?q="
    private val searchSettings = "&src=1&ch=1&sortby=news_read&resorder=desc&num=40&page=1"

    override fun makeSearchURL(reqestText: String): String {
        return searchPage + reqestText.replace(' ', '+') + searchSettings
    }

    override fun parseSearchPage(pageContent: Document): ArrayList<SongSearchItem> {
        val elements = pageContent.select("ul.b-listing") //[class=b-listing]
            .eq(0)
            .select("li[class=b-listing__item]")
        val songItems = ArrayList<SongSearchItem>()
        elements.forEach { element ->
            val songItem = element.select("a[class=b-listing__item__link]").eq(0)
            val itemText = songItem.text()
            var splitIndex = itemText.indexOfAny(listOf(" - ", " – ", " — ", " — "))
            val artist: String
            if (splitIndex != -1) {
                artist = itemText.substring(0, splitIndex)
                splitIndex += 3
            } else {
                artist = "Неизвестен"
                splitIndex = 0
            }
            val title = itemText.substring(splitIndex)
            val link = "https://mychords.net/" + songItem.attr("href")
            songItems.add(SongSearchItem(artist, title, link))
        }
        return songItems
    }


    override fun parseLyricsPage(pageContent: Document): String {
        var text = pageContent.select("pre.w-words__text")
            .eq(0).html().toString()
        text = text.replace("</a></span>", "</b>")
            .replace("<span class=\"b-accord__symbol\"><a\\b[^>]*>\">".toRegex(), "<b>")
            .replace("(Смотреть видео)", "")
            .replace("<a\\b[^>]*>Взято с сайта https://mychords.net</a>".toRegex(), "")
        return text
    }
}