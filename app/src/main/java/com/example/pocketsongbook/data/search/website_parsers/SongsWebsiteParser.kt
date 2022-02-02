package com.example.pocketsongbook.data.search.website_parsers

import com.example.pocketsongbook.domain.models.FoundSongModel
import com.example.pocketsongbook.domain.models.SongModel

interface SongsWebsiteParser {

    suspend fun loadSearchResults(searchRequest: String): List<FoundSongModel>

    suspend fun loadSong(foundSong: FoundSongModel): SongModel

}