package com.example.pocketsongbook.data.search.website_parsers

import com.example.pocketsongbook.domain.models.FoundSongModel
import com.example.pocketsongbook.domain.search.SongsWebsite
import org.jsoup.nodes.Document
import javax.inject.Inject

class MychordsWebsiteParser @Inject constructor() : BaseWebsiteParser() {

    override val website = SongsWebsite.MyChords

    private val baseUrl by lazy { "https://hm6.ru/" }

    private val searchUrl by lazy { "search?q=" }

    private val searchSettings by lazy { "&src=1&sortby=news_read&resorder=desc&num=40&page=1" }

    override fun buildSearchURL(searchQuery: String): String {
        return baseUrl + searchUrl + searchQuery.replace(' ', '+') + searchSettings
    }

    override fun parseSearchPage(pageContent: Document): List<FoundSongModel> {
        val elements = pageContent.select("ul.b-listing") //[class=b-listing]
            .eq(0)
            .select("li[class=b-listing__item]")
        val songItems = ArrayList<FoundSongModel>()
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
            val link = baseUrl + songItem.attr("href")
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
        val element = pageContent.select("pre[itemprop=text]")

        val rawHtmlText = element.eq(0).html()

        return rawHtmlText.replace("</a></span>", "</b>")
            .replace("<span class=\"b-accord__symbol\"><a\\b[^>]*>\">".toRegex(), "<b>")
            .replace("(Смотреть видео)", "")
            .replace("<a\\b[^>]*>Взято с сайта https://mychords.net</a>".toRegex(), "")
    }
}