package com.example.pocketsongbook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.text.HtmlCompat
import com.example.pocketsongbook.data_classes.Song
import kotlinx.android.synthetic.main.activity_song_view.*
import java.lang.StringBuilder
import java.util.regex.Pattern

class SongViewActivity : AppCompatActivity() {
    private val songLines: ArrayList<Pair<String, Boolean>> = ArrayList()
    private lateinit var songText: String
    private lateinit var chordsSet: Set<String>
    private var transposeAmount: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song_view)
        songText =
            this.intent.getStringExtra("lyrics")
                .replace("\n", "<br>\n")
                .replace(" ", "&nbsp;")
        setSongText(
            Song(
                intent.getStringExtra("artist"),
                intent.getStringExtra("title"),
                songText
            )
        )
        initChordsSet()
        setOnClickListeners()


    }

    private fun setSongText(song: Song) {
        artist_text_view.text = song.artist
        title_text_view.text = song.title
        song_text_view.text =
            HtmlCompat.fromHtml(song.lyrics, HtmlCompat.FROM_HTML_MODE_COMPACT)
    }

    private fun setOnClickListeners() {
        button_plus.setOnClickListener {
            if (transposeAmount < 6) transposeAmount += 1
            transposeSongChords(transposeAmount)
            amount_text_view.text = transposeAmount.toString()
        }
        button_minus.setOnClickListener {
            if (transposeAmount > -6) transposeAmount -= 1
            transposeSongChords(transposeAmount)
            amount_text_view.text = transposeAmount.toString()
        }
    }

    private fun initChordsSet() {
        val patternChord =
            Pattern.compile("<b>(.*?)</b>")
        val matcher = patternChord.matcher(songText)
        val chordsFound = ArrayList<String>()
        while (matcher.find()) {
            val chord = matcher.group(1)
            chordsFound.add(chord)
        }
        chordsSet = chordsFound.toSet()
    }

    private fun transposeSongChords(amount: Int) {
        val transposedChords = mutableMapOf<String, String>()
        chordsSet.forEach { chord ->
            transposedChords[chord] = ChordsTransponder.transposeChord(chord, amount)
        }
        val newTextBuilder = StringBuilder()
        var chordStartIndex = songText.indexOf("<b>")
        var prevChordEnd = 0
        while (chordStartIndex != -1) {
            val chordEnd = songText.indexOf("</b>", chordStartIndex + 3)
            val chord =
                songText.substring(
                    chordStartIndex + 3,
                    chordEnd
                )
            newTextBuilder.append(songText.substring(prevChordEnd, chordStartIndex + 3))
            newTextBuilder.append(transposedChords[chord])
            newTextBuilder.append("</b>")
            prevChordEnd = chordEnd + 4
            chordStartIndex = songText.indexOf("<b>", prevChordEnd)
        }
        newTextBuilder.append(songText.substring(prevChordEnd))
        song_text_view.text =
            HtmlCompat.fromHtml(newTextBuilder.toString(), HtmlCompat.FROM_HTML_MODE_COMPACT)
    }

}
