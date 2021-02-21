package com.example.pocketsongbook.feature.favourites

import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pocketsongbook.R
import com.example.pocketsongbook.common.BaseFragment
import com.example.pocketsongbook.common.navigation.toFragment
import com.example.pocketsongbook.common.navigation.toScreen
import com.example.pocketsongbook.data.favorites.FavoriteSongModel
import com.example.pocketsongbook.data.models.SongModel
import com.example.pocketsongbook.feature.song.SongFragment
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_favourites.*
import moxy.ktx.moxyPresenter
import javax.inject.Inject

// TODO: 18.07.20 отображать сайт сохранённой песни
// TODO: 18.07.20 добавить возможность выбрать сортировку по времени добавления

class FavouritesFragment : BaseFragment(R.layout.fragment_favourites), FavouritesView,
    SearchView.OnQueryTextListener {

    @Inject
    lateinit var favouritesPresenter: FavouritesPresenter

    private val presenter by moxyPresenter { favouritesPresenter }

    private val favoriteSongItemsAdapter =
        FavoriteSongsAdapter(
            onItemClickResponse = {
                presenter.onSongClicked(it)
            })

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolbar()
        setUpRecycler()
    }

    private fun setUpRecycler() {
        favouritesRv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = favoriteSongItemsAdapter
        }
    }

    private fun setUpToolbar() {
        searchViewFavourites.setOnQueryTextListener(this)
    }

    override fun updateItems(newItems: List<FavoriteSongModel>) {
        favoriteSongItemsAdapter.submitList(newItems)
    }

    override fun navigateToSong(song: SongModel) {
        router.navigateTo(SongFragment.SongArgs(song).toFragment().toScreen())
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) presenter.onQueryTextChanged(query)
        favouritesToolbar.clearFocus()
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null) presenter.onQueryTextChanged(newText)
        return true
    }

}