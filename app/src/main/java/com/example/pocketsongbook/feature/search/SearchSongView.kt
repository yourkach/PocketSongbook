package com.example.pocketsongbook.feature.search

import com.example.pocketsongbook.common.BaseView
import com.ybond.core_entities.models.SongModel
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.SkipStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(SkipStrategy::class)
interface SearchSongView : BaseView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun toSongScreen(song: SongModel)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun toFavouritesScreen()

//  view State

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun renderViewState(state: SearchViewState)

//    errors

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showFailedToLoadSongError()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showSearchFailedError()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showInternetConnectionError()

}