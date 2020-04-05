package com.example.pocketsongbook

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pocketsongbook.async_tasks.SearchPerformingTask
import com.example.pocketsongbook.async_tasks.SongDownloadTask
import com.example.pocketsongbook.data_classes.SongSearchItem
import com.example.pocketsongbook.data_classes.Song
import com.example.pocketsongbook.interfaces.SearchFinishResponse
import com.example.pocketsongbook.interfaces.SongClickResponse
import com.example.pocketsongbook.interfaces.SongDownloadResponse
import com.example.pocketsongbook.interfaces.WebSiteHandler
import com.example.pocketsongbook.webSiteHandlers.AmDmHandler
import com.example.pocketsongbook.webSiteHandlers.MyChordsHandler
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener,
    AdapterView.OnItemSelectedListener,
    SearchFinishResponse, SongClickResponse, SongDownloadResponse {

    private lateinit var webSiteHandler: WebSiteHandler
    private lateinit var searchItemsAdapter: SearchRecyclerAdapter
    private lateinit var siteHandlersList: List<Pair<String, WebSiteHandler>>
    private val searchItems = ArrayList<SongSearchItem>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initRecyclerView()
        initWebSiteHandlersMap()
        initSpinner()
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        //TODO(idk just do some shit)
        toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.website_select -> {
                if (spinner_website_select.visibility == View.VISIBLE) {
                    hideWebsiteSelector()
                } else if (spinner_website_select.visibility != View.VISIBLE) {
                    showWebsiteSelector()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun hideWebsiteSelector(){
        spinner_website_select.visibility = View.GONE
    }

    private fun showWebsiteSelector(){
        spinner_website_select.visibility = View.VISIBLE
    }

    private fun initRecyclerView() {
        recycler_view.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            searchItemsAdapter = SearchRecyclerAdapter(this@MainActivity)
            adapter = searchItemsAdapter
            addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
        }
        searchItemsAdapter.submitList(searchItems)
    }

    private fun initWebSiteHandlersMap() {
        siteHandlersList = listOf(
            "AmDm.ru" to AmDmHandler(),
            "MyChords.net" to MyChordsHandler()
        )
        webSiteHandler = AmDmHandler()
    }

    private fun initSpinner() {
        spinner_website_select.apply {
            adapter =
                ArrayAdapter<String>(
                    this@MainActivity,
                    R.layout.spinner_item,
                    siteHandlersList.map {
                        it.first
                    }
                )
            onItemSelectedListener = this@MainActivity
            setSelection(0)
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            toolbar.clearFocus()
            performSearch(query)
            return true
        }
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean = true

    private fun performSearch(searchRequest: String) {
        hideWebsiteSelector()
        if (searchRequest != "") {
            val task =
                SearchPerformingTask(
                    webSiteHandler,
                    this
                )
            task.execute(searchRequest)
            showLoadingPanel()
        } else {
            Toast.makeText(this, "Empty search request!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSearchFinished(searchResult: ArrayList<SongSearchItem>?) {
        searchItems.clear()
        hideLoadingPanel()
        when {
            searchResult == null -> {
                Toast.makeText(this, "Internet connection error!", Toast.LENGTH_SHORT).show()
            }
            searchResult.isNotEmpty() -> {
                searchResult.forEach { item -> searchItems.add(item) }
            }
            else -> {
                Toast.makeText(this, "Nothing found!", Toast.LENGTH_SHORT).show()
            }
        }
        searchItemsAdapter.notifyDataSetChanged()
    }

    override fun onSongClicked(pos: Int) {
        val downloadTask =
            SongDownloadTask(
                webSiteHandler,
                this
            )
        downloadTask.execute(searchItems[pos])
        showLoadingPanel()
    }

    override fun onSongDownloadFinish(song: Song?) {
        when (song) {
            null -> {
                Toast.makeText(this, "Failed to download song!", Toast.LENGTH_SHORT).show()
            }
            else -> {
                val intent = Intent(this, SongViewActivity::class.java)
                val bundle = Bundle()
                bundle.putParcelable("song", song)
                intent.putExtra("bundle", bundle)
                startActivity(intent)
            }
        }
        hideLoadingPanel()
    }

    private fun showLoadingPanel(){
        if (loading_panel.visibility != View.VISIBLE) {
            loading_panel.visibility = View.VISIBLE
        }
    }

    private fun hideLoadingPanel(){
        if (loading_panel.visibility != View.GONE) {
            loading_panel.visibility = View.GONE
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) = Unit

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        webSiteHandler = siteHandlersList[position].second
    }


}

