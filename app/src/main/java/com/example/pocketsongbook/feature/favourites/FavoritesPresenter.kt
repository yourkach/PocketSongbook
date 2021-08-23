package com.example.pocketsongbook.feature.favourites

import com.example.pocketsongbook.common.BasePresenter
import com.example.pocketsongbook.common.BaseView
import com.example.pocketsongbook.common.extensions.setAndCancelJob
import com.example.pocketsongbook.data.favorites.FavoriteSongModel
import com.example.pocketsongbook.domain.event_bus.Event
import com.example.pocketsongbook.domain.event_bus.EventBus
import com.ybond.core.entities.SongModel
import com.example.pocketsongbook.feature.favourites.usecase.GetFavoriteSongsUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import moxy.InjectViewState
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.SkipStrategy
import moxy.viewstate.strategy.StateStrategyType
import javax.inject.Inject


@StateStrategyType(AddToEndSingleStrategy::class)
interface FavouritesView : BaseView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun updateItems(newItems: List<FavoriteSongModel>)

    @StateStrategyType(SkipStrategy::class)
    fun navigateToSong(song: SongModel)
}


@InjectViewState
class FavoritesPresenter @Inject constructor(
    private val getFavoriteSongsUseCase: GetFavoriteSongsUseCase,
    private val eventBus: EventBus
) : BasePresenter<FavouritesView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        getAndShowSongs(ObtainSongsOption.All)
        subscribeToEvents()
    }

    private fun subscribeToEvents() {
        launch {
            eventBus.eventsFlow.collect {
                if (it is Event.FavoritesChange) {
                    getAndShowSongs(lastOption)
                }
            }
        }
    }

    fun onSongClicked(songModel: FavoriteSongModel) {
        viewState.navigateToSong(songModel.song)
    }

    private var lastOption: ObtainSongsOption = ObtainSongsOption.All
    private var getSongsJob by setAndCancelJob()
    private fun getAndShowSongs(option: ObtainSongsOption) {
        getSongsJob = launch {
            val items = getFavoriteSongsUseCase(option)
            lastOption = option
            viewState.updateItems(items)
        }
    }

    fun onQueryTextChanged(query: String) {
        val option = when {
            query.isNotBlank() -> ObtainSongsOption.ByQuery(query)
            else -> ObtainSongsOption.All
        }
        getAndShowSongs(option)
    }

}

sealed class ObtainSongsOption {
    data class ByQuery(val query: String) : ObtainSongsOption()
    object All : ObtainSongsOption()
}