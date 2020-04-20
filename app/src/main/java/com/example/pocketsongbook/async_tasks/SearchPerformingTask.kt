package com.example.pocketsongbook.async_tasks

import android.os.AsyncTask
import com.example.pocketsongbook.data_classes.SongSearchItem
import com.example.pocketsongbook.interfaces.WebSiteHandler
import com.example.pocketsongbook.presenter.SearchPresenter
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