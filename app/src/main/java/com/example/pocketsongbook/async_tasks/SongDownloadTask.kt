package com.example.pocketsongbook.async_tasks

import android.os.AsyncTask
import com.example.pocketsongbook.data_classes.SongSearchItem
import com.example.pocketsongbook.data_classes.Song
import com.example.pocketsongbook.interfaces.WebSiteHandler
import com.example.pocketsongbook.presenter.SearchPresenter
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