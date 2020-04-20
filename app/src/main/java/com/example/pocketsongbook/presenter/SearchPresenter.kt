package com.example.pocketsongbook.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.pocketsongbook.async_tasks.SearchPerformingTask
import com.example.pocketsongbook.async_tasks.SongDownloadTask
import com.example.pocketsongbook.data_classes.Song
import com.example.pocketsongbook.data_classes.SongSearchItem
import com.example.pocketsongbook.interfaces.SongSearchView
import com.example.pocketsongbook.interfaces.WebSiteHandler
import com.example.pocketsongbook.website_handlers.AmDmHandler
import com.example.pocketsongbook.website_handlers.MyChordsHandler

@InjectViewState
class SearchPresenter : MvpPresenter<SongSearchView>() {

    private lateinit var webSiteHandler: WebSiteHandler
    private lateinit var siteHandlersList: List<Pair<String, WebSiteHandler>>
    private var searchQuery: String = ""
    private val searchItems = ArrayList<SongSearchItem>()

    init {
        initWebSiteHandlersList()
    }

    private fun initWebSiteHandlersList() {
        siteHandlersList = listOf(
            "MyChords.net" to MyChordsHandler(),
            "AmDm.ru" to AmDmHandler()
        )
        webSiteHandler = siteHandlersList.first().second
    }

    fun getSpinnerItems(): List<String> {
        return siteHandlersList.map {
            it.first
        }
    }

    fun performSearch(query: String) {
        searchQuery = query
        if (query != "") {
            val task =
                SearchPerformingTask(
                    webSiteHandler,
                    this
                )
            task.execute(query)
            viewState.showLoadingPanel(true)
        } else {
            viewState.showToast("Empty search request!")
        }
    }

    fun onSearchFinished(searchResult: ArrayList<SongSearchItem>?) {
        searchItems.clear()
        viewState.showLoadingPanel(false)
        when {
            searchResult == null -> {
                viewState.showToast("Internet connection error!")
            }
            searchResult.isNotEmpty() -> {
                searchResult.forEach { item -> searchItems.add(item) }
            }
            else -> {
                viewState.showToast("Nothing found!")
            }
        }
        viewState.updateRecyclerItems(searchItems)
    }

    fun onSongClicked(pos: Int) {
        viewState.enableRecyclerView(false)
        val downloadTask =
            SongDownloadTask(
                webSiteHandler,
                this
            )
        downloadTask.execute(searchItems[pos])
        viewState.showLoadingPanel(true)
    }

    fun onSongDownloadFinish(song: Song?) {
        when (song) {
            null -> {
                viewState.showToast("Failed to download song!")
            }
            else -> {
                viewState.startSongViewActivity(song)
            }
        }
        viewState.showLoadingPanel(false)
        viewState.enableRecyclerView(true)
    }

    fun onSpinnerItemSelected(pos: Int) {
        webSiteHandler = siteHandlersList[pos].second
        if (searchQuery != "") {
            performSearch(searchQuery)
        }
    }
}