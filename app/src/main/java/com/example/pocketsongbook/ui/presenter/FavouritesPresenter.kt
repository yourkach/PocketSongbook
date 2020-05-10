package com.example.pocketsongbook.ui.presenter

import com.example.pocketsongbook.domain.FavouriteSongsDao
import com.example.pocketsongbook.domain.model.Song
import com.example.pocketsongbook.domain.model.SongEntity
import com.example.pocketsongbook.ui.view.FavouritesView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import moxy.InjectViewState
import moxy.MvpPresenter
import javax.inject.Inject

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