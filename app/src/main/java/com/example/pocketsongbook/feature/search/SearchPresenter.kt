package com.example.pocketsongbook.feature.search

import com.example.pocketsongbook.R
import com.example.pocketsongbook.data.models.SongModel
import com.example.pocketsongbook.data.models.FoundSongModel
import com.example.pocketsongbook.common.BasePresenter
import com.example.pocketsongbook.common.BaseView
import com.example.pocketsongbook.common.extensions.setAndCancelJob
import com.example.pocketsongbook.domain.SongsWebsite
import com.example.pocketsongbook.feature.search.usecase.GetSearchResultsUseCase
import com.example.pocketsongbook.feature.search.usecase.LoadSongModelUseCase
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import moxy.InjectViewState
import moxy.presenterScope
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.SkipStrategy
import moxy.viewstate.strategy.StateStrategyType
import javax.inject.Inject
import javax.inject.Singleton


@StateStrategyType(SkipStrategy::class)
interface SearchSongView : BaseView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun toSongScreen(song: SongModel)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun toFavouritesScreen()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setWebsiteSelected(website: SongsWebsite)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun setSearchItems(newItems: List<FoundSongModel>)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showFailedToLoadSongError()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun dismissWebsitesSelector()

}

@InjectViewState
@Singleton
class SearchPresenter @Inject constructor(
    private val getSearchResultsUseCase: GetSearchResultsUseCase,
    private val loadSongModelUseCase: LoadSongModelUseCase
) : BasePresenter<SearchSongView>() {

    private var lastSearchQuery: String? = null
    private var selectedWebsite: SongsWebsite = SongsWebsite.AmDm
    set(value) {
        if(field == value) return
        field = value
        viewState.setWebsiteSelected(field)
        lastSearchQuery?.let(::startSearchJob)
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        startCollectingQuery()
        viewState.setWebsiteSelected(selectedWebsite)
    }

    private val searchQueryFlow = MutableSharedFlow<String>()

    fun onQueryTextChange(newText: String) {
        presenterScope.launch {
            searchQueryFlow.emit(newText)
        }
    }

    private fun startCollectingQuery() {
        launch {
            searchQueryFlow
                .distinctUntilChanged()
                .debounce(700)
                .collect { query ->
                    startSearchJob(query)
                }
        }
    }


    var searchJob: Job? by setAndCancelJob()
    private fun startSearchJob(query: String) {
        searchJob = launch {
            withLoading {
                lastSearchQuery = query
                val searchResult = getSearchResultsUseCase(selectedWebsite, query)
                viewState.setSearchItems(searchResult)
            }
        }
    }

    fun onWebsiteSelected(website: SongsWebsite) {
        selectedWebsite = website
        launch {
            delay(100)
            viewState.dismissWebsitesSelector()
        }
    }

    private var loadSongJob: Job? = null
    fun onSongClicked(searchItem: FoundSongModel) {
        if (loadSongJob?.isActive != true) {
            loadSongJob = launch {
                withLoading {
                    loadSongModelUseCase(searchItem)?.let { song ->
                        viewState.toSongScreen(song)
                    } ?: let {
                        viewState.showFailedToLoadSongError()
                    }
                }
            }
        }
    }

    override fun onFailure(e: Throwable) {
        viewState.showMessage(R.string.error_no_connection)
    }

    fun onFavouritesClicked() {
        viewState.toFavouritesScreen()
    }
}
