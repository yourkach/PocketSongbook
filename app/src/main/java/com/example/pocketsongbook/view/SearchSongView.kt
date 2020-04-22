package com.example.pocketsongbook.view

import com.example.pocketsongbook.data.Song
import com.example.pocketsongbook.data.SongSearchItem
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.SkipStrategy
import moxy.viewstate.strategy.StateStrategyType


@StateStrategyType(SkipStrategy::class)
interface SearchSongView : MvpView {

    @StateStrategyType(SkipStrategy::class)
    fun showLoadingPanel(visible: Boolean)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun updateRecyclerItems(newItems: ArrayList<SongSearchItem>)

    @StateStrategyType(SkipStrategy::class)
    fun showToast(text: String)

    @StateStrategyType(SkipStrategy::class)
    fun startSongViewActivity(song: Song)

    @StateStrategyType(SkipStrategy::class)
    fun startFavouritesActivity()
}