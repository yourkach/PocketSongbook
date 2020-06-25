package com.example.pocketsongbook.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Toast
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pocketsongbook.App
import com.example.pocketsongbook.R
import com.example.pocketsongbook.domain.model.Song
import com.example.pocketsongbook.domain.model.SongSearchItem
import com.example.pocketsongbook.ui.activity.FavouritesActivity
import com.example.pocketsongbook.ui.activity.SongViewActivity
import com.example.pocketsongbook.ui.adapter.SearchAdapter
import com.example.pocketsongbook.ui.presenter.SearchPresenter
import com.example.pocketsongbook.ui.view.SearchSongView
import kotlinx.android.synthetic.main.activity_search.*
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import javax.inject.Inject

class SearchFragment : MvpAppCompatFragment(R.layout.fragment_search), SearchSongView {

    @Inject
    lateinit var searchPresenter: SearchPresenter

    private val presenter by moxyPresenter { searchPresenter }

    private lateinit var searchItemsAdapter: SearchAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        App.appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setUpRecyclerView()
        setUpSpinner()
        setUpToolbar()
    }

    private fun setUpToolbar() {
        //activity?.actionBar TODO
        activity?.getSupportActionBar()
        setSupportActionBar(searchToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        searchToolbar.setNavigationIcon(R.drawable.ic_star_border_white_24dp)
        searchToolbar.setNavigationOnClickListener {
            presenter.onFavouritesClicked()
        }
    }

    override fun showToast(messageId: Int) {
        Toast.makeText(context, getString(messageId), Toast.LENGTH_SHORT).show()
    }

    override fun navigateToSongView(song: Song) {
        //TODO
    }

    override fun navigateToFavourites() {
        //TODO
    }


    private fun setUpRecyclerView() {
        searchRv.apply {
            layoutManager = LinearLayoutManager(context)
            searchItemsAdapter =
                SearchAdapter { position ->
                    presenter.onSongClicked(position)
                }
            adapter = searchItemsAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }

    private fun setUpSpinner() {
        searchWebsiteSelector.apply {
            adapter =
                ArrayAdapter(
                    context,
                    R.layout.spinner_item, presenter.getSpinnerItems()
                )
            onItemSelectedListener = TODO("make item selected listener") //this@SearchSongActivity
        }
    }

    /*
    override fun onNothingSelected(parent: AdapterView<*>?) = Unit

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        presenter.onSpinnerItemSelected(position)
    }

 */

    /*
    TODO
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search_options_menu, menu)
        val item = menu.findItem(R.id.search)
        val searchView: SearchView = item.actionView as SearchView
        searchView.setOnQueryTextListener(this)
        searchView.queryHint = HtmlCompat.fromHtml(
            "<font color = #ffffff>" + getString(R.string.search_hint) + "</font>",
            HtmlCompat.FROM_HTML_MODE_COMPACT
        )
        return true
    }

     */

    override fun onQueryTextSubmit(query: String?): Boolean {
        return presenter.onQueryTextSubmit(query)
    }

    override fun clearToolbarFocus() {
        searchToolbar.clearFocus()
    }

    override fun onQueryTextChange(newText: String?): Boolean = true


    override fun showLoadingPanel(visible: Boolean) {
        if (visible) {
            searchLoadingPanel.visibility = View.VISIBLE
        } else {
            searchLoadingPanel.visibility = View.GONE
        }
    }

    override fun updateRecyclerItems(newItems: List<SongSearchItem>) {
        searchItemsAdapter.apply {
            setList(newItems)
        }
    }



}