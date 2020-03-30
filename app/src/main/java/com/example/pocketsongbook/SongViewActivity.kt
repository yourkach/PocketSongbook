package com.example.pocketsongbook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.text.HtmlCompat
import com.example.pocketsongbook.data_classes.SongText
import kotlinx.android.synthetic.main.activity_song_view.*
import java.lang.StringBuilder
import java.util.regex.Pattern

class SongViewActivity : AppCompatActivity() {
    private val songLines: ArrayList<Pair<String, Boolean>> = ArrayList()
    private lateinit var songTextAdapter: SongTextRecyclerAdapter
    private lateinit var songText: String
    private lateinit var chordsSet: Set<String>
    private var transposeAmount: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song_view)
        val intent = this.intent
        songText =
            intent.getStringExtra("text")
                .replace("\n", "<br>\n")
                .replace(" ", "&nbsp;")


        val song = SongText(
            intent.getStringExtra("artist"),
            intent.getStringExtra("title"),
            songText
        )

        artistTextView.text = song.artist
        titleTextView.text = song.songTitle
        songTextView.text =
            HtmlCompat.fromHtml(song.lyrics, HtmlCompat.FROM_HTML_MODE_COMPACT)

        initChordsSet()

        buttonPlus.setOnClickListener {
            if (transposeAmount < 6) transposeAmount += 1
            transpose(transposeAmount)
            amountTextView.text = transposeAmount.toString()
        }

        buttonMinus.setOnClickListener {
            if (transposeAmount > -6) transposeAmount -= 1
            transpose(transposeAmount)
            amountTextView.text = transposeAmount.toString()
        }
    }

    fun initChordsSet() {
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

    fun transpose(amount: Int) {
        var newText : String = songText
        chordsSet.forEach { chord ->
            newText = newText.replace(
                "<b>$chord</b>",
                "<b>${ChordsTransponder.transposeChord(chord, amount)}</b>"
            )
        }
        songTextView.text =
            HtmlCompat.fromHtml(newText, HtmlCompat.FROM_HTML_MODE_COMPACT)
    }

}
