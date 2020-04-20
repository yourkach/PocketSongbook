package com.example.pocketsongbook.ui.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.pocketsongbook.data.Song
import com.example.pocketsongbook.misc.ChordsTransponder
import com.example.pocketsongbook.view.SongView
import java.util.regex.Pattern


@InjectViewState
class SongPresenter : MvpPresenter<SongView>() {

    private lateinit var song: Song
    private lateinit var keyDefaultLabel: String
    private lateinit var fontDefaultLabel: String
    private lateinit var chordsSet: Set<String>
    private var chordsKey: Int = 0
    private var currentFontSize: Float = FONT_SIZE_DEFAULT

    fun setData(
        song: Song,
        keyDefaultLabel: String,
        fontDefaultLabel: String
    ) {
        this.keyDefaultLabel = keyDefaultLabel
        this.fontDefaultLabel = fontDefaultLabel
        this.song = song
        initChordsSet()
        transposeLyrics(chordsKey)
        viewState.setArtistLabelText(song.artist)
        viewState.setTitleLabelText(song.title)
    }


    private fun initChordsSet() {
        val patternChord =
            Pattern.compile("<b>(.*?)</b>")
        val matcher = patternChord.matcher(song.lyrics)
        val chordsFound = ArrayList<String>()
        while (matcher.find()) {
            val chord = matcher.group(1)
            if (chord != null) chordsFound.add(chord)
        }
        chordsSet = chordsFound.toSet()
    }

    private fun changeFontSize(increase: Boolean = true, setDefaultSize: Boolean = false) {
        val newFontSize = when {
            setDefaultSize -> {
                FONT_SIZE_DEFAULT
            }
            else -> {
                currentFontSize + if (increase) FONT_CHANGE_AMOUNT else (-FONT_CHANGE_AMOUNT)
            }
        }
        if (newFontSize in FONT_SIZE_MIN..FONT_SIZE_MAX) {
            currentFontSize = newFontSize
            viewState.setLyricsFontSize(newFontSize)
            val fontSizeLabelText =
                if (currentFontSize == FONT_SIZE_DEFAULT) {
                    fontDefaultLabel
                } else {
                    currentFontSize.toInt().toString()
                }
            viewState.setFontSizeLabelText(fontSizeLabelText)
        }

    }

    fun onFontPlusClicked() {
        changeFontSize(increase = true)
    }

    fun onFontLabelClicked() {
        changeFontSize(setDefaultSize = true)
    }

    fun onFontMinusClicked() {
        changeFontSize(increase = false)
    }

    fun onKeyUpClicked() {
        chordsKey = (chordsKey + 1) % 12
        transposeLyrics(chordsKey)
        val keyLabel = if (chordsKey == 0) keyDefaultLabel else chordsKey.toString()
        viewState.setKeyLabelText(keyLabel)
    }

    fun onKeyLabelClicked() {
        chordsKey = 0
        transposeLyrics(chordsKey)
        val keyLabel = keyDefaultLabel
        viewState.setKeyLabelText(keyLabel)
    }

    fun onKeyDownClicked() {
        chordsKey = (chordsKey - 1) % 12
        transposeLyrics(chordsKey)
        val keyLabel = if (chordsKey == 0) keyDefaultLabel else chordsKey.toString()
        viewState.setKeyLabelText(keyLabel)
    }


    private fun transposeLyrics(amount: Int) {
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
        val transposedLyrics = newTextBuilder.toString()
        viewState.setSongLyrics(transposedLyrics)
    }


    companion object {
        private const val FONT_CHANGE_AMOUNT: Float = 2.0F
        const val FONT_SIZE_DEFAULT: Float = 16.0F
        const val FONT_SIZE_MIN: Float = 8.0F
        const val FONT_SIZE_MAX: Float = 36.0F
    }
}