package com.example.pocketsongbook.interfaces

import com.example.pocketsongbook.data_classes.Song

interface SongDownloadResponse {
    fun onSongDownloadFinish(song: Song?)
}