package com.example.pocketsongbook.website_handlers

import com.example.pocketsongbook.data.SongSearchItem
import org.jsoup.nodes.Document

interface WebSiteHandler {
    fun buildSearchURL(searchQuery: String): String

    fun parseSearchPage(pageContent: Document): ArrayList<SongSearchItem>

    fun parseLyricsPage(pageContent: Document): String
}