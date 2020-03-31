package com.example.pocketsongbook.interfaces

import com.example.pocketsongbook.data_classes.SongSearchItem

interface SearchFinishResponse {
    fun onSearchFinished(searchResult: ArrayList<SongSearchItem>?)
}
