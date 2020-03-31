package com.example.pocketsongbook

import com.example.pocketsongbook.data_classes.SongSearchItem
import com.example.pocketsongbook.webSiteHandlers.AmDmHandler
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.IOException

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun jsoupSearchParseTest() {
        try {

            val handler =
                AmDmHandler()
            val searchUrl = handler.makeSearchURL("Спи в заброшенном")
            val doc: Document = Jsoup.connect(searchUrl).get()
            val elements = doc.select("table.items")
                .eq(0)
                .select("td.artist_name")
            val songsFound = ArrayList<SongSearchItem>()
            elements.forEach { element ->
                val artist = element.select("a[class=artist]").eq(0).text()
                val title = element.select("a[class=artist]").eq(1).text()
                val link = element.select("a[class=artist]").eq(1).attr("href")
                songsFound.add(SongSearchItem(artist, title, link))
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    @Test
    fun jsoupSongParseTest() {
        try {
            val handler =
                AmDmHandler()
            val searchUrl = "https://amdm.ru/akkordi/splin/2523/liniya_zhizni/"
            val doc: Document = Jsoup.connect(searchUrl).get()
            val songText = doc.select("pre[itemprop=chordsBlock]")
                .eq(0).html().toString()
            println(songText)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
