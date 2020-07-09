package com.example.pocketsongbook.ui.fragments.favourites

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pocketsongbook.App
import com.example.pocketsongbook.R
import com.example.pocketsongbook.data.database.SongEntity
import com.example.pocketsongbook.data.models.Song
import com.example.pocketsongbook.ui.adapter.FavouritesAdapter
import kotlinx.android.synthetic.main.fragment_favourites.*
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import javax.inject.Inject
import javax.inject.Singleton

class FavouritesFragment : MvpAppCompatFragment(R.layout.fragment_favourites), FavouritesView,
    SearchView.OnQueryTextListener {


    @Inject
    lateinit var favouritesPresenter: FavouritesPresenter

    private val presenter by moxyPresenter { favouritesPresenter }

    private val favouritesAdapter = FavouritesAdapter(onItemClickResponse = {
        presenter.onSongClicked(it)
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        App.appComponent.inject(this)
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
            adapter = favouritesAdapter
            addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
        }
    }

    private fun setUpToolbar() {
        favouritesGoBackIv.setOnClickListener {
            findNavController().popBackStack()
        }
        searchViewFavourites.setOnQueryTextListener(this)
    }

    override fun updateItems(newItems: List<SongEntity>) {
        favouritesAdapter.setList(newItems)
    }

    override fun clearToolbarFocus() {
        favouritesToolbar.clearFocus()
    }

    override fun navigateToSong(song: Song) {
        findNavController().navigate(
            FavouritesFragmentDirections.actionFavouritesFragmentToSongFragment(song)
        )
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) presenter.onQuerySubmit(query)
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null) presenter.onQueryTextChanged(newText)
        return true
    }

}