package com.example.pocketsongbook.ui

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
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.example.pocketsongbook.R
import com.example.pocketsongbook.data_classes.SongSearchItem
import com.example.pocketsongbook.data_classes.Song
import com.example.pocketsongbook.interfaces.*
import com.example.pocketsongbook.presenter.SearchPresenter
import kotlinx.android.synthetic.main.activity_search.*


class SongSearchActivity : MvpAppCompatActivity(), SearchView.OnQueryTextListener,
    AdapterView.OnItemSelectedListener, SongSearchView {

    @InjectPresenter
    lateinit var presenter: SearchPresenter


    private lateinit var searchItemsAdapter: SearchRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        initRecyclerView()
        initSpinner()
        setSupportActionBar(searchToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        //TODO(navigation to favourites activity)
        searchToolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp)
    }

    override fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    override fun startSongViewActivity(song: Song) {
        val intent = Intent(this, SongViewActivity::class.java)
        val bundle = Bundle()
        bundle.putParcelable("song", song)
        intent.putExtra("bundle", bundle)
        startActivity(intent)
    }

    override fun enableRecyclerView(enabled: Boolean) {
        TODO()
    }

    private fun initRecyclerView() {
        searchRecycler.apply {
            layoutManager = LinearLayoutManager(this@SongSearchActivity)
            searchItemsAdapter =
                SearchRecyclerAdapter(presenter)
            adapter = searchItemsAdapter
            addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
        }
    }

    private fun initSpinner() {
        searchWebsiteSelector.apply {
            adapter =
                ArrayAdapter(
                    this@SongSearchActivity,
                    R.layout.spinner_item, presenter.getSpinnerItems()
                )
            onItemSelectedListener = this@SongSearchActivity
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        val item = menu.findItem(R.id.search)
        val searchView: SearchView = item.actionView as SearchView
        searchView.setOnQueryTextListener(this)
        searchView.queryHint = HtmlCompat.fromHtml(
            "<font color = #ffffff>" + getString(R.string.search_hint) + "</font>",
            HtmlCompat.FROM_HTML_MODE_COMPACT
        )
        return true
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            searchToolbar.clearFocus()
            presenter.performSearch(query)
            return true
        }
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean = true


    override fun showLoadingPanel(visible: Boolean) {
        if (visible) {
            searchLoadingPanel.visibility = View.VISIBLE
        } else {
            searchLoadingPanel.visibility = View.GONE
        }
    }

    override fun updateRecyclerItems(newItems: ArrayList<SongSearchItem>) {
        searchItemsAdapter.submitList(newItems)
        searchItemsAdapter.notifyDataSetChanged()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) = Unit

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        presenter.onSpinnerItemSelected(position)
    }


}

