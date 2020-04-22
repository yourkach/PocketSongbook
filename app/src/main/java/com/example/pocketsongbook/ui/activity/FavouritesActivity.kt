package com.example.pocketsongbook.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.SearchView
import androidx.core.text.HtmlCompat
import com.example.pocketsongbook.R
import com.example.pocketsongbook.data.SongSearchItem
import com.example.pocketsongbook.ui.presenter.FavouritesPresenter
import com.example.pocketsongbook.view.FavouritesView
import kotlinx.android.synthetic.main.activity_favourites.*
import moxy.MvpAppCompatActivity
import moxy.presenter.InjectPresenter

class FavouritesActivity : MvpAppCompatActivity(), FavouritesView, SearchView.OnQueryTextListener {

    @InjectPresenter
    lateinit var presenter: FavouritesPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourites)
        setUpToolbar()
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
            setDisplayShowTitleEnabled(false)
        }
        favouritesToolbar.setNavigationOnClickListener {
            finish()
        }
    }

    override fun updateItems(newItems: List<SongSearchItem>) {
        TODO("Not yet implemented")
    }

    override fun clearToolbarFocus() {
        favouritesToolbar.clearFocus()
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
