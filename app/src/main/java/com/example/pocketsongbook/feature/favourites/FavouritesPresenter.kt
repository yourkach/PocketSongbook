package com.example.pocketsongbook.feature.favourites

import com.example.pocketsongbook.data.models.Song
import com.example.pocketsongbook.data.database.SongEntity
import com.example.pocketsongbook.common.BasePresenter
import com.example.pocketsongbook.common.BaseView
import com.example.pocketsongbook.feature.favourites.usecase.GetFavouriteSongsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import moxy.InjectViewState
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.SkipStrategy
import moxy.viewstate.strategy.StateStrategyType
import javax.inject.Inject


@StateStrategyType(AddToEndSingleStrategy::class)
interface FavouritesView : BaseView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun updateItems(newItems: List<SongEntity>)

    @StateStrategyType(SkipStrategy::class)
    fun navigateToSong(song: Song)
}


@InjectViewState
class FavouritesPresenter @Inject constructor(
    private val getFavouriteSongsUseCase: GetFavouriteSongsUseCase
) : BasePresenter<FavouritesView>() {

//    private lateinit var items: List<SongEntity>

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        launch {
            val items = withContext(Dispatchers.IO) {
                getFavouriteSongsUseCase(GetFavouriteSongsUseCase.Param.All)
            }
            viewState.updateItems(items)
        }
    }

    fun onSongClicked(songEntity: SongEntity) {
        viewState.navigateToSong(Song(songEntity))
    }

    fun onQueryTextChanged(newText: String) {
        launch {
            val items = withContext(Dispatchers.IO) {
                if (newText.isNotEmpty()) {
                    getFavouriteSongsUseCase(GetFavouriteSongsUseCase.Param.ByQuery(newText))
                } else {
                    getFavouriteSongsUseCase(GetFavouriteSongsUseCase.Param.All)
                }
            }
            viewState.updateItems(items)
        }
//        CoroutineScope(Dispatchers.IO).launch {
//            items = favouriteSongsDao.findByName(newText)
//            withContext(Dispatchers.Main) {
//                viewState.updateItems(items)
//            }
//        }
    }

}