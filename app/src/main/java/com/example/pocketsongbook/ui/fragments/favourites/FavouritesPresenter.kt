package com.example.pocketsongbook.ui.fragments.favourites

import com.example.pocketsongbook.domain.database.FavouriteSongsDao
import com.example.pocketsongbook.domain.models.Song
import com.example.pocketsongbook.domain.database.SongEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import moxy.InjectViewState
import moxy.MvpPresenter
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
    fun clearToolbarFocus()


    @StateStrategyType(SkipStrategy::class)
    fun startSongViewActivity(song: Song)
}


@InjectViewState
class FavouritesPresenter @Inject constructor(private val favouriteSongsDao: FavouriteSongsDao) :
    MvpPresenter<FavouritesView>() {

    private lateinit var items: List<SongEntity>

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                items = favouriteSongsDao.getAll()
            }
            viewState.updateItems(items)
        }
    }

    fun onSongClicked(position: Int) {
        viewState.startSongViewActivity(Song(items[position]))
    }

    fun onQueryTextChanged(newText: String) {
        CoroutineScope(Dispatchers.IO).launch {
            items = favouriteSongsDao.findByName(newText)
            withContext(Dispatchers.Main) {
                viewState.updateItems(items)
            }
        }
    }

    fun onQuerySubmit(query: String) {
        viewState.clearToolbarFocus()
        CoroutineScope(Dispatchers.IO).launch {
            items = favouriteSongsDao.findByName(query)
            withContext(Dispatchers.Main) {
                viewState.updateItems(items)
            }
        }
    }
}