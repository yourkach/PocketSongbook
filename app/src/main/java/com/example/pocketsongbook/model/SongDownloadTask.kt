package com.example.pocketsongbook.model

import android.os.AsyncTask
import com.example.pocketsongbook.data.SongSearchItem
import com.example.pocketsongbook.data.Song
import com.example.pocketsongbook.website_handlers.WebSiteHandler
import com.example.pocketsongbook.ui.presenter.SearchPresenter
import org.jsoup.Jsoup
import java.io.IOException

class SongDownloadTask(
    private val handler: WebSiteHandler,
    private val presenter: SearchPresenter
) :
    AsyncTask<SongSearchItem, Void, Song?>() {

    override fun doInBackground(vararg params: SongSearchItem): Song? {
        val song = params[0]
        return try {
            val document = Jsoup.connect(song.link).get()
            val lyrics = handler.parseLyricsPage(document)
                .replace("\n", "<br>\n")
                .replace(" ", "&nbsp;")
            Song(song.artist, song.title, lyrics, song.link)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    override fun onPostExecute(result: Song?) {
        presenter.onSongDownloadFinish(result)
        super.onPostExecute(result)
    }

}