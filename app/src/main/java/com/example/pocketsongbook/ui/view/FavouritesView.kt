package com.example.pocketsongbook.ui.view

import com.example.pocketsongbook.domain.model.Song
import com.example.pocketsongbook.domain.model.SongEntity
import com.example.pocketsongbook.domain.model.SongSearchItem
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.SkipStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface FavouritesView : MvpView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun updateItems(newItems: List<SongEntity>)

    @StateStrategyType(SkipStrategy::class)
    fun clearToolbarFocus()


    @StateStrategyType(SkipStrategy::class)
    fun startSongViewActivity(song: Song)
}
