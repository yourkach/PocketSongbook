package com.example.pocketsongbook.data.song_repos

import com.example.pocketsongbook.domain.model.Song
import com.example.pocketsongbook.domain.model.SongSearchItem

interface SongsRepo {
    val websiteName: String
    suspend fun getSearchResults(searchRequest: String): List<SongSearchItem>?
    suspend fun getSong(songSearchItem: SongSearchItem): Song?
}