package com.example.pocketsongbook.domain.api.websites_api

import com.example.pocketsongbook.domain.models.Song
import com.example.pocketsongbook.domain.models.SongSearchItem
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException

class MychordsWebsiteApi :
    SongsWebsiteApi {

    override val websiteName: String = "MyChords.ru"

    private val baseUrl = "https://mychords.net/search?q="

    private val searchSettings = "&src=1&ch=1&sortby=news_read&resorder=desc&num=40&page=1"

    private fun buildSearchURL(searchQuery: String): String {
        return baseUrl + searchQuery.replace(' ', '+') + searchSettings
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
            null
        }
    }

    private fun parseLyricsPage(pageContent: Document): String {
        var text = pageContent.select("pre.w-words__text")
            .eq(0).html().toString()
        text = text.replace("</a></span>", "</b>")
            .replace("<span class=\"b-accord__symbol\"><a\\b[^>]*>\">".toRegex(), "<b>")
            .replace("(Смотреть видео)", "")
            .replace("<a\\b[^>]*>Взято с сайта https://mychords.net</a>".toRegex(), "")
        return text
    }
}