package com.example.pocketsongbook.view

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface SongView : MvpView {

    fun setKeyLabelText(text : String)

    fun setFontSizeLabelText(text : String)

    fun setArtistLabelText(text : String)

    fun setTitleLabelText(text : String)

    fun setSongLyrics(lyricsHtml: String)

    fun setLyricsFontSize(fontSize : Float)
}