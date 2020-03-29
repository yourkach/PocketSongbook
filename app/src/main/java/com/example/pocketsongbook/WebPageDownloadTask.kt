package com.example.pocketsongbook

import android.os.AsyncTask
import com.example.pocketsongbook.interfaces.AsyncResponse
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class WebPageDownloadTask(private val delegate: AsyncResponse? = null) :
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
        delegate?.processFinish(result)
        super.onPostExecute(result)
    }
}