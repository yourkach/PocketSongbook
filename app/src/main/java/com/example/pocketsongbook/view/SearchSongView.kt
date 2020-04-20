package com.example.pocketsongbook.view

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.example.pocketsongbook.data.Song
import com.example.pocketsongbook.data.SongSearchItem


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

}