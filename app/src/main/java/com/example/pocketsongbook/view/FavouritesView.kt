package com.example.pocketsongbook.view

import com.example.pocketsongbook.data.SongSearchItem
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.SkipStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface FavouritesView : MvpView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun updateItems(newItems: List<SongSearchItem>)

    @StateStrategyType(SkipStrategy::class)
    fun clearToolbarFocus()
}
