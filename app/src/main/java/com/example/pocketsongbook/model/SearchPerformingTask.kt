package com.example.pocketsongbook.model

import android.os.AsyncTask
import com.example.pocketsongbook.data.SongSearchItem
import com.example.pocketsongbook.website_handlers.WebSiteHandler
import com.example.pocketsongbook.ui.presenter.SearchPresenter
import org.jsoup.Jsoup
import java.io.IOException

class SearchPerformingTask(
    private val handler: WebSiteHandler,
    private val presenter: SearchPresenter
) :
    AsyncTask<String, Void, ArrayList<SongSearchItem>?>() {

    override fun doInBackground(vararg params: String): ArrayList<SongSearchItem>? {
        val searchRequest = params[0]
        return try {
            val document = Jsoup.connect(handler.buildSearchURL(searchRequest)).get()
            handler.parseSearchPage(document)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    override fun onPostExecute(result: ArrayList<SongSearchItem>?) {
        presenter.onSearchFinished(result)
        super.onPostExecute(result)
    }
}