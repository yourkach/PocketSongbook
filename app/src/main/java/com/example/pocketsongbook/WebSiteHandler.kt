package com.example.pocketsongbook

interface WebSiteHandler {
    fun makeSearchURL(text: String): String

    fun updateSearchItemsList(pageContent: String, searchItemsList: ArrayList<ItemSong>)

    fun getParsedSongPageText(pageContent: String): ArrayList<String>
}