package com.example.pocketsongbook.interfaces

import android.security.keystore.SecureKeyImportUnavailableException
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.example.pocketsongbook.data_classes.Song
import com.example.pocketsongbook.data_classes.SongSearchItem


@StateStrategyType(SkipStrategy::class)
interface SongSearchView : MvpView {

    @StateStrategyType(SkipStrategy::class)
    fun showLoadingPanel(visible: Boolean)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun updateRecyclerItems(newItems: ArrayList<SongSearchItem>)

    @StateStrategyType(SkipStrategy::class)
    fun showToast(text: String)

    @StateStrategyType(SkipStrategy::class)
    fun startSongViewActivity(song: Song)

    @StateStrategyType(SkipStrategy::class)
    fun enableRecyclerView(enabled: Boolean)
}