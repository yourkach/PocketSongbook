package com.example.pocketsongbook.ui.view

import com.example.pocketsongbook.domain.model.Song
import com.example.pocketsongbook.domain.model.SongSearchItem
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.SkipStrategy
import moxy.viewstate.strategy.StateStrategyType


@StateStrategyType(SkipStrategy::class)
interface SearchSongView : MvpView {

    @StateStrategyType(SkipStrategy::class)
    fun showLoadingPanel(visible: Boolean)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun updateRecyclerItems(newItems: List<SongSearchItem>)

    @StateStrategyType(SkipStrategy::class)
    fun showToast(messageId: Int)

    @StateStrategyType(SkipStrategy::class)
    fun navigateToSongView(song: Song)

    @StateStrategyType(SkipStrategy::class)
    fun navigateToFavourites()

    @StateStrategyType(SkipStrategy::class)
    fun clearToolbarFocus()
}