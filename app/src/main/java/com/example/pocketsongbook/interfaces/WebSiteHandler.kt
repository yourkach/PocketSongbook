package com.example.pocketsongbook.interfaces

import com.example.pocketsongbook.data_classes.SongSearchItem
import org.jsoup.nodes.Document

interface WebSiteHandler {
    fun buildSearchURL(searchQuery: String): String

    fun parseSearchPage(pageContent: Document): ArrayList<SongSearchItem>

    fun parseLyricsPage(pageContent: Document): String
}