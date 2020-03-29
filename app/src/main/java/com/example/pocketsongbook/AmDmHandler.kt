package com.example.pocketsongbook

import android.util.Log
import com.example.pocketsongbook.data_classes.SongViewItem
import com.example.pocketsongbook.interfaces.WebSiteHandler
import java.util.regex.Pattern

class AmDmHandler : WebSiteHandler {
    private val searchPage = "https://amdm.ru/search/?q="

    override fun makeSearchURL(text: String): String {
        return searchPage + text.replace(' ', '+')
    }

    override fun updateSearchItemsList(pageContent: String, searchItemsList: ArrayList<SongViewItem>) {
        val patternOnePiece =
            Pattern.compile("(?s)<td class=\"artist_name\"><a href=(.*?)<div class=\"b-favorite-over\">")
        val matcherOnePiece = patternOnePiece.matcher(pageContent)
        val patternSongLink =
            Pattern.compile(" — <a href=\"(.*?)\" class=\"artist\">")
        val patternArtist =
            Pattern.compile("class=\"artist\">(.*?)</a> —")
        val patternTitle =
            Pattern.compile("<a href=\".*\" class=\"artist\">(.*?)</a><div class=\"b-favorite-over\">")
        while (matcherOnePiece.find()) {
            val onePiece = matcherOnePiece.group(0).replace("\n".toRegex(), "")
            Log.i("OnePiece", onePiece)
            val matcherSongLink = patternSongLink.matcher(onePiece)
            val matcherArtist = patternArtist.matcher(onePiece)
            val matcherTitle = patternTitle.matcher(onePiece)
            if (matcherArtist.find() && matcherSongLink.find() && matcherTitle.find()) {
                val songLink = "https:" + matcherSongLink.group(1)
                val artist = matcherArtist.group(1)
                val title = matcherTitle.group(1)
                val newSong = SongViewItem(
                    artist,
                    title,
                    songLink
                )
                searchItemsList.add(newSong)
            }
        }
    }

    override fun getParsedSongPageText(pageContent: String): ArrayList<String> {
        val patternSong =
            Pattern.compile("(?s)<pre itemprop=\"chordsBlock\">(.*?)</pre>")
        val matcherSong = patternSong.matcher(pageContent)
        var text: String? = null
        if (matcherSong.find()) {
            text = matcherSong.group(1).replace("<b>([^<]*)</b>".toRegex(), "$1")
        }
        return if (text != null) text.split("\n") as ArrayList<String> else arrayListOf("text parse error")
    }
}