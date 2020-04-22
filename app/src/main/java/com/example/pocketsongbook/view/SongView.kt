package com.example.pocketsongbook.view

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface SongView : MvpView {

    fun setKeyLabelText(text : String = "", setDefault: Boolean = false)

    fun setFontSizeLabelText(text : String = "", setDefault: Boolean = false)

    fun setArtistLabelText(text : String)

    fun setTitleLabelText(text : String)

    fun setSongLyrics(lyricsHtml: String)

    fun setLyricsFontSize(fontSize : Float)
}