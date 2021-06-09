package com.example.pocketsongbook.data.search.website_parsers

import com.ybond.core.models.FoundSongModel
import com.ybond.core.models.SongModel
import com.ybond.core.models.SongsWebsite
interface SongsWebsiteParser {

    val website: SongsWebsite

    suspend fun loadSearchResults(searchRequest: String): List<FoundSongModel>

    suspend fun loadSong(foundSong: FoundSongModel): SongModel

}