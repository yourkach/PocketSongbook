package com.example.pocketsongbook

import android.os.AsyncTask
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL


class MainActivity : AppCompatActivity() {

    private lateinit var textAdapter: TextRecyclerAdapter
    private val searchItems = ArrayList<ItemSong>()

    private val AmDm: AmDmHandler = AmDmHandler()

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
            textAdapter = TextRecyclerAdapter()
            adapter = textAdapter
            addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
        }
        textAdapter.submitList(searchItems)
    }

    private fun performSearch() {
        val searchRequest = search_edit_text.text.toString()

        //SHIT
        val task = DownloadTask(AmDm, textAdapter, searchItems)
        val pageContent = task.execute(AmDm.makeSearchURL(searchRequest))

        textAdapter.notifyDataSetChanged()
    }

    //JUST TRY
    internal class DownloadTask(
        private val handler: WebSiteHandler,
        private val adapter: TextRecyclerAdapter,
        private val searchItems: ArrayList<ItemSong>
    ) :
        AsyncTask<String?, Void?, String>() {

        override fun doInBackground(vararg params: String?): String {
            val result = StringBuilder()
            var url: URL? = null
            var urlConnection: HttpURLConnection? = null
            try {
                url = URL(params[0])
                urlConnection = url.openConnection() as HttpURLConnection
                val `in` = urlConnection.inputStream
                val reader = InputStreamReader(`in`)
                val bufferedReader = BufferedReader(reader)
                var line = bufferedReader.readLine()
                while (line != null) {
                    result.append(line).append("\n")
                    line = bufferedReader.readLine()
                }
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                urlConnection?.disconnect()
            }
            return result.toString()
        }

        override fun onPostExecute(result: String?) {
            if (result != null) {
                handler.updateSearchItemsList(result, searchItems)
                adapter.notifyDataSetChanged()
            }
            super.onPostExecute(result)
        }
    }
}
