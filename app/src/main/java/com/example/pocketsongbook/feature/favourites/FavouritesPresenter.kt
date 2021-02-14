package com.example.pocketsongbook.feature.favourites

import com.example.pocketsongbook.data.models.SongModel
import com.example.pocketsongbook.common.BasePresenter
import com.example.pocketsongbook.common.BaseView
import com.example.pocketsongbook.feature.favourites.usecase.GetFavouriteSongsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import moxy.InjectViewState
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.SkipStrategy
import moxy.viewstate.strategy.StateStrategyType
import javax.inject.Inject


@StateStrategyType(AddToEndSingleStrategy::class)
interface FavouritesView : BaseView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun updateItems(newItems: List<SongModel>)

    @StateStrategyType(SkipStrategy::class)
    fun navigateToSong(song: SongModel)
}


@InjectViewState
class FavouritesPresenter @Inject constructor(
    private val getFavouriteSongsUseCase: GetFavouriteSongsUseCase
) : BasePresenter<FavouritesView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        launch {
            val items = withContext(Dispatchers.IO) {
                getFavouriteSongsUseCase(GetFavouriteSongsUseCase.Param.All)
            }
            viewState.updateItems(items)
        }
    }

    fun onSongClicked(song: SongModel) {
        viewState.navigateToSong(song)
    }

    fun onQueryTextChanged(newText: String) {
        launch {
            val items = withContext(Dispatchers.IO) {
                getFavouriteSongsUseCase(
                    if (newText.isNotEmpty()) GetFavouriteSongsUseCase.Param.ByQuery(newText)
                    else GetFavouriteSongsUseCase.Param.All
                )
            }
            viewState.updateItems(items)
        }
    }

}