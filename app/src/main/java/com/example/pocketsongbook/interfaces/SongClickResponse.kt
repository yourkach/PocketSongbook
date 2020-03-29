package com.example.pocketsongbook.interfaces

interface SongClickResponse {
    fun onSelectedSongDownloaded(pos: Int, songPageContent: String)
}