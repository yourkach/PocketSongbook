package com.example.pocketsongbook

import com.example.pocketsongbook.data_classes.SongSearchItem
import com.example.pocketsongbook.webSiteHandlers.AmDmHandler
import com.example.pocketsongbook.webSiteHandlers.MyChordsHandler
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
                MyChordsHandler()
            /*
            val request = "романс"
            val searchUrl = handler.makeSearchURL(request)
            val doc: Document = Jsoup.connect(searchUrl).get()
            val resItems = handler.parseSearchPage(doc)
            resItems.forEach {
                println(it)
            }
            */
            val url = "https://mychords.net/prikolnye_pesni/7081-vot-i-pomer-ded-maksim.html"
            val doc: Document = Jsoup.connect(url).get()
            println(handler.parseLyricsPage(doc))
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @Test
    fun mathTest() {
        println(12 % 12)
        println(10 % 12)
        println(-12 % 12)
        println(-1 % 12)
        println(-11 % 12)

    }
}
