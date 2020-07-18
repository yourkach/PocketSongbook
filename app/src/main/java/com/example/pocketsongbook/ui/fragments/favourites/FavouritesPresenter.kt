package com.example.pocketsongbook.ui.fragments.favourites

import com.example.pocketsongbook.data.models.Song
import com.example.pocketsongbook.data.database.SongEntity
import com.example.pocketsongbook.ui.fragments.BasePresenter
import com.example.pocketsongbook.ui.fragments.favourites.usecase.GetFavouriteSongsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import moxy.InjectViewState
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.SkipStrategy
import moxy.viewstate.strategy.StateStrategyType
import javax.inject.Inject


@StateStrategyType(AddToEndSingleStrategy::class)
interface FavouritesView : MvpView {

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
        doInMainContext {
            val items = withContext(Dispatchers.IO) {
                getFavouriteSongsUseCase(GetFavouriteSongsUseCase.Param.GetAll)
            }
            viewState.updateItems(items)
        }
    }

    fun onSongClicked(songEntity: SongEntity) {
        viewState.navigateToSong(Song(songEntity))
    }

    fun onQueryTextChanged(newText: String) {
        doInMainContext {
            val items = withContext(Dispatchers.IO) {
                if (newText.isNotEmpty()) {
                    getFavouriteSongsUseCase(GetFavouriteSongsUseCase.Param.GetByQuery(newText))
                } else {
                    getFavouriteSongsUseCase(GetFavouriteSongsUseCase.Param.GetAll)
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