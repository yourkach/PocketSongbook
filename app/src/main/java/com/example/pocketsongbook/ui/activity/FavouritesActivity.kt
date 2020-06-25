package com.example.pocketsongbook.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.SearchView
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pocketsongbook.App
import com.example.pocketsongbook.R
import com.example.pocketsongbook.domain.model.Song
import com.example.pocketsongbook.domain.model.SongEntity
import com.example.pocketsongbook.domain.model.SongSearchItem
import com.example.pocketsongbook.ui.adapter.FavouritesAdapter
import com.example.pocketsongbook.ui.adapter.SearchAdapter
import com.example.pocketsongbook.ui.presenter.FavouritesPresenter
import com.example.pocketsongbook.ui.view.FavouritesView
import kotlinx.android.synthetic.main.activity_favourites.*
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter
import javax.inject.Inject
/*

class FavouritesActivity : MvpAppCompatActivity(),
    FavouritesView, SearchView.OnQueryTextListener {

    @Inject
    lateinit var favouritesPresenter: FavouritesPresenter

    private val presenter by moxyPresenter { favouritesPresenter }

    private val favouritesAdapter = FavouritesAdapter(onItemClickResponse = {
        presenter.onSongClicked(it)
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        App.appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourites)
        setUpToolbar()
        setUpRecycler()
    }

    private fun setUpRecycler() {
        favouritesRv.apply {
            layoutManager = LinearLayoutManager(this@FavouritesActivity)
            adapter = favouritesAdapter
            addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.favourites_options_menu, menu)
        val item = menu.findItem(R.id.favouritesSearch)
        val searchView: SearchView = item.actionView as SearchView
        searchView.setOnQueryTextListener(this)
        searchView.queryHint = HtmlCompat.fromHtml(
            "<font color = #ffffff>" + getString(R.string.search_hint) + "</font>",
            HtmlCompat.FROM_HTML_MODE_COMPACT
        )
        return true
    }

    private fun setUpToolbar() {
        setSupportActionBar(favouritesToolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = getString(R.string.label_favourites)
        }
        favouritesToolbar.setNavigationOnClickListener {
            finish()
        }
    }

    override fun updateItems(newItems: List<SongEntity>) {
        favouritesAdapter.setList(newItems)
    }

    override fun clearToolbarFocus() {
        favouritesToolbar.clearFocus()
    }

    override fun startSongViewActivity(song: Song) {
        val intent = Intent(this, SongViewActivity::class.java)
        intent.putExtra(SONG_KEY, song)
        startActivity(intent)
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
*/
