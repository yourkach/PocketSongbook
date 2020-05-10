package com.example.pocketsongbook.ui.view

import com.example.pocketsongbook.domain.model.Chord
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface SongView : MvpView {


    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setKeyLabelText(text: String = "")

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setKeyLabelText(resId : Int)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setFontSizeLabelText(text: String = "")

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setFontSizeLabelText(resId : Int)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setArtistLabelText(text: String)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setTitleLabelText(text: String)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setSongLyrics(lyricsHtml: String)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setLyricsFontSize(fontSize: Float)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setFavouritesButtonFilled(filled: Boolean)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun loadChords(chords : List<Chord>)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun openChordBar()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun closeChordBar()
}