package com.example.pocketsongbook.interfaces

import com.example.pocketsongbook.data_classes.SongViewItem

interface WebSiteHandler {
    fun makeSearchURL(text: String): String

    fun updateSearchItemsList(pageContent: String, searchItemsList: ArrayList<SongViewItem>)

    fun getParsedSongPageText(pageContent: String): ArrayList<String>
}