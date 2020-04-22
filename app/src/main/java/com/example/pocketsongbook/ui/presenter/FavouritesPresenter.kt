package com.example.pocketsongbook.ui.presenter

import com.example.pocketsongbook.view.FavouritesView
import moxy.InjectViewState
import moxy.MvpPresenter

@InjectViewState
class FavouritesPresenter : MvpPresenter<FavouritesView>() {


    fun onQueryTextChanged(newText: String) {

    }

    fun onQuerySubmit(query: String) {
        viewState.clearToolbarFocus()
    }
}