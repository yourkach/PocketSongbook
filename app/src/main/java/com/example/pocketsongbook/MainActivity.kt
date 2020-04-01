package com.example.pocketsongbook

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
import com.example.pocketsongbook.webSiteHandlers.MychordsHandler
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(),
    SearchFinishResponse, SongClickResponse, SongDownloadResponse {

    private lateinit var webSiteHandler: WebSiteHandler
    private lateinit var searchItemsAdapter: SearchRecyclerAdapter
    private val searchItems = ArrayList<SongSearchItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initRecyclerView()
        search_edit_text.setOnEditorActionListener(OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch()
                return@OnEditorActionListener true
            }
            false
        })
        webSiteHandler = MychordsHandler() //TODO("switching between handlers")
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

    private fun performSearch() {
        val searchRequest = search_edit_text.text.toString()
        hideKeyboard()

        if (searchRequest != "") {
            val task =
                SearchPerformingTask(
                    webSiteHandler,
                    this
                )
            task.execute(searchRequest)
            loadingPanel.visibility = View.VISIBLE
        } else {
            Toast.makeText(this, "Empty search reqest!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun hideKeyboard() {
        search_edit_text.clearFocus()
        val inp: InputMethodManager =
            getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inp.hideSoftInputFromWindow(search_edit_text.windowToken, 0)
    }

    override fun onSearchFinished(searchResult: ArrayList<SongSearchItem>?) {
        searchItems.clear()
        loadingPanel.visibility = View.GONE
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
        loadingPanel.visibility = View.VISIBLE
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
                /*
                intent.putExtra("artist", song.artist)
                intent.putExtra("title", song.title)
                intent.putExtra("lyrics", song.lyrics)
                 */
                startActivity(intent)
            }
        }
        loadingPanel.visibility = View.GONE
    }

}

