package com.example.pocketsongbook

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pocketsongbook.data_classes.SongViewItem
import com.example.pocketsongbook.interfaces.AsyncResponse
import com.example.pocketsongbook.interfaces.SongClickResponse
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(),
    AsyncResponse, SongClickResponse {

    private lateinit var searchItemsAdapter: SearchRecyclerAdapter
    private val searchItems = ArrayList<SongViewItem>()

    private val amDmHandler: AmDmHandler = AmDmHandler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initRecyclerView()
        search_edit_text.setOnEditorActionListener(OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchItems.clear()
                performSearch()
                return@OnEditorActionListener true
            }
            false
        })
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

        search_edit_text.clearFocus()
        val inp : InputMethodManager =
            getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inp.hideSoftInputFromWindow(search_edit_text.windowToken, 0)

        if (searchRequest != "") {
            val task = WebPageDownloadTask(this)
            task.execute(amDmHandler.makeSearchURL(searchRequest))
        } else {
            searchItems.clear()
            searchItemsAdapter.notifyDataSetChanged()
        }
    }


    override fun processFinish(result: String?) {
        when {
            result != null -> {
                searchItems.clear()
                amDmHandler.updateSearchItemsList(result, searchItems)
                searchItemsAdapter.notifyDataSetChanged()
            }
            searchItems.isEmpty() -> {
                Toast.makeText(this, "Nothing found!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onSelectedSongDownloaded(pos: Int, songPageContent: String) {
        val song = searchItems[pos]
        val intent = Intent(this, SongViewActivity::class.java)
        intent.putExtra("artist", song.artist)
        intent.putExtra("title", song.title)
        intent.putExtra("text", amDmHandler.getParsedSongPageText(songPageContent))
        startActivity(intent)
    }

}

