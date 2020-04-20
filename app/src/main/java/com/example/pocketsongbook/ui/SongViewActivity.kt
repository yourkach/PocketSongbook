package com.example.pocketsongbook.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import androidx.core.text.HtmlCompat
import com.example.pocketsongbook.misc.ChordsTransponder
import com.example.pocketsongbook.R
import com.example.pocketsongbook.data_classes.Song
import kotlinx.android.synthetic.main.activity_song_view.*
import java.lang.StringBuilder
import java.util.regex.Pattern

class SongViewActivity : AppCompatActivity() {
    private lateinit var song: Song
    private lateinit var songText: String
    private lateinit var chordsSet: Set<String>

    private var chordsKey: Int = 0

    private var currentFontSize: Float = FONT_SIZE_DEFAULT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song_view)
        song = this.intent.getBundleExtra("bundle")?.getParcelable("song")!!
        songText = song.lyrics
        setSongText(song)
        initChordsSet()
        setOnClickListeners()
    }

    //TODO("adjustable auto scrolling in Scrollview ")
    //TODO("songs saving")

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putFloat("font_size", currentFontSize)
        outState.putInt("key", chordsKey)
        outState.putString("song_text", songText)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        currentFontSize = savedInstanceState.getFloat("font_size")
        songLyricsTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, currentFontSize)
        updateFontSizeLabel()

        chordsKey = savedInstanceState.getInt("key")
        updateKeyLabel()

        songText = savedInstanceState.getString("song_text").toString()
        updateLyricsText()
        super.onRestoreInstanceState(savedInstanceState)
    }


    private fun initChordsSet() {
        val patternChord =
            Pattern.compile("<b>(.*?)</b>")
        val matcher = patternChord.matcher(songText)
        val chordsFound = ArrayList<String>()
        while (matcher.find()) {
            val chord = matcher.group(1)
            if (chord != null) chordsFound.add(chord)
        }
        chordsSet = chordsFound.toSet()
    }

    private fun setSongText(song: Song) {
        songArtistTv.text = song.artist
        songTitleTv.text = song.title
        songLyricsTv.text =
            HtmlCompat.fromHtml(song.lyrics, HtmlCompat.FROM_HTML_MODE_COMPACT)
    }

    private fun setOnClickListeners() {
        songKeyUpBtn.setOnClickListener {
            chordsKey = (chordsKey + 1) % 12
            transposeSongChords(chordsKey)
            updateKeyLabel()
        }
        songKeyDownBtn.setOnClickListener {
            chordsKey = (chordsKey - 1) % 12
            transposeSongChords(chordsKey)
            updateKeyLabel()
        }
        songKeyAmountTv.setOnClickListener {
            chordsKey = 0
            transposeSongChords(chordsKey)
            updateKeyLabel()
        }
        songFontPlusBtn.setOnClickListener {
            changeFontSize(amount = 2.0F)
        }
        songFontMinusBtn.setOnClickListener {
            changeFontSize(amount = -2.0F)
        }
        songFontSizeTv.setOnClickListener {
            changeFontSize(setDefaultSize = true)
        }
    }

    private fun changeFontSize(amount: Float = 0F, setDefaultSize: Boolean = false) {
        val newFontSize = when {
            setDefaultSize -> {
                FONT_SIZE_DEFAULT
            }
            else -> {
                currentFontSize + amount
            }
        }
        if (newFontSize in FONT_SIZE_MIN..FONT_SIZE_MAX) {
            currentFontSize = newFontSize
            songLyricsTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, currentFontSize)
            updateFontSizeLabel()
        }
    }

    private fun updateFontSizeLabel() {
        when {
            currentFontSize != FONT_SIZE_DEFAULT -> {
                songFontSizeTv.text = currentFontSize.toInt().toString()
            }
            else -> {
                songFontSizeTv.text = getString(R.string.font)
            }
        }
    }

    private fun updateKeyLabel() {
        when (chordsKey) {
            0 -> {
                songKeyAmountTv.text = getString(R.string.key)
            }
            else -> {
                songKeyAmountTv.text = chordsKey.toString()
            }
        }
    }

    private fun transposeSongChords(amount: Int) {
        val transposedChords = mutableMapOf<String, String>()

        chordsSet.forEach { chord ->
            transposedChords[chord] =
                ChordsTransponder.transposeChord(
                    chord,
                    amount
                )
        }
        val lyrics = song.lyrics
        val newTextBuilder = StringBuilder()
        var chordStartIndex = lyrics.indexOf("<b>")
        var prevChordEnd = 0
        while (chordStartIndex != -1) {
            val chordEnd = lyrics.indexOf("</b>", chordStartIndex + 3)
            val chord =
                lyrics.substring(
                    chordStartIndex + 3,
                    chordEnd
                )
            newTextBuilder.append(lyrics.substring(prevChordEnd, chordStartIndex + 3))
            newTextBuilder.append(transposedChords[chord])
            newTextBuilder.append("</b>")
            prevChordEnd = chordEnd + 4
            chordStartIndex = lyrics.indexOf("<b>", prevChordEnd)
        }
        newTextBuilder.append(lyrics.substring(prevChordEnd))
        songText = newTextBuilder.toString()
        updateLyricsText()
    }


    private fun updateLyricsText() {
        songLyricsTv.text = HtmlCompat.fromHtml(songText, HtmlCompat.FROM_HTML_MODE_COMPACT)
    }

    companion object {
        const val FONT_SIZE_DEFAULT: Float = 16.0F
        const val FONT_SIZE_MIN: Float = 8.0F
        const val FONT_SIZE_MAX: Float = 36.0F
    }
}
