package com.example.pocketsongbook.ui.presenter

import com.example.pocketsongbook.ChordsTransponder
import com.example.pocketsongbook.R
import com.example.pocketsongbook.domain.FavouriteSongsDao
import com.example.pocketsongbook.domain.model.Chord
import com.example.pocketsongbook.domain.model.Song
import com.example.pocketsongbook.domain.model.SongEntity
import com.example.pocketsongbook.ui.view.SongView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import moxy.InjectViewState
import moxy.MvpPresenter
import java.util.regex.Pattern
import javax.inject.Inject


@InjectViewState
class SongPresenter(private val favouriteSongsDao: FavouriteSongsDao, private val song: Song) :
    MvpPresenter<SongView>() {

    private lateinit var chordsSet: Set<String>
    private var transposedChordsList: List<String>
    private var transposedLyrics: String = ""
    private var isFavourite: Boolean = false
    private var chordsKey: Int = 0
    private var currentFontSize: Float = FONT_SIZE_DEFAULT
    private var chordsBarOpened = false

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        if (transposedLyrics == "") transposedLyrics = getTransposedLyrics(chordsKey)
        updateViewAfterTransposing()
        viewState.setArtistLabelText(song.artist)
        viewState.setTitleLabelText(song.title)
        updateChordsImages()

        CoroutineScope(Dispatchers.IO).launch {
            isFavourite = favouriteSongsDao.findByUrl(song.link).isNotEmpty()
            withContext(Dispatchers.Main) {
                viewState.setFavouritesButtonFilled(isFavourite)
            }
        }
    }

    init {
        initChordsSet()
        transposedChordsList = chordsSet.toList()
    }

    private fun initChordsSet() {
        val chordPattern =
            Pattern.compile("<b>(.*?)</b>")
        val matcher = chordPattern.matcher(song.lyrics)
        val chordsFound = mutableListOf<String>()
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

            if (currentFontSize == FONT_SIZE_DEFAULT) {
                viewState.setFontSizeLabelText(R.string.song_font_default)
            } else {
                viewState.setFontSizeLabelText(currentFontSize.toInt().toString())
            }
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
        transposedLyrics = getTransposedLyrics(chordsKey)
        updateViewAfterTransposing()
    }

    fun onKeyLabelClicked() {
        chordsKey = 0
        transposedLyrics = getTransposedLyrics(chordsKey)
        updateViewAfterTransposing()
    }

    fun onKeyDownClicked() {
        chordsKey = (chordsKey - 1) % 12
        transposedLyrics = getTransposedLyrics(chordsKey)
        updateViewAfterTransposing()
    }

    private fun updateViewAfterTransposing() {
        viewState.setSongLyrics(transposedLyrics)
        updateKeyLabel()
        updateChordsImages()
    }

    fun onFavouritesButtonClicked() {
        if (isFavourite) {
            removeFromFavourites()
            viewState.setFavouritesButtonFilled(false)
        } else {
            addToFavourites()
            viewState.setFavouritesButtonFilled(true)
        }
    }

    fun onFloatingButtonClicked() {
        if (chordsBarOpened) {
            viewState.closeChordBar()
        } else {
            viewState.openChordBar()
        }
        chordsBarOpened = !chordsBarOpened
    }

    private fun addToFavourites() {
        CoroutineScope(Dispatchers.IO).launch {
            favouriteSongsDao.insert(SongEntity(song))
        }
        isFavourite = true
    }

    private fun removeFromFavourites() {
        CoroutineScope(Dispatchers.IO).launch {
            favouriteSongsDao.deleteByUrl(song.link)
        }
        isFavourite = false
    }

    private fun getTransposedLyrics(amount: Int): String {
        return if (amount != 0) {
            val newTransposed = mutableListOf<String>()
            val transposedChords = mutableMapOf<String, String>()
            chordsSet.forEach {
                val newChord = ChordsTransponder.transposeChord(
                    it,
                    amount
                )
                transposedChords[it] = newChord
                newTransposed.add(newChord)
            }
            transposedChordsList = newTransposed
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
            newTextBuilder.toString()
        } else {
            transposedChordsList = chordsSet.toList()
            song.lyrics
        }
    }

    private fun updateKeyLabel() {
        if (chordsKey == 0) {
            viewState.setKeyLabelText(R.string.song_key_default)
        } else {
            viewState.setKeyLabelText(chordsKey.toString())
        }
    }

    private fun updateChordsImages() {
        val newChords = transposedChordsList.map {
            Chord(
                it,
                "https://mychords.net/i/img/akkords/${it.replace("#", "sharp")}.png"
            )
        }
        viewState.loadChords(newChords)
    }

    companion object {
        private const val FONT_CHANGE_AMOUNT: Float = 2.0F
        const val FONT_SIZE_DEFAULT: Float = 16.0F
        const val FONT_SIZE_MIN: Float = 8.0F
        const val FONT_SIZE_MAX: Float = 36.0F
    }
}


class SongPresenterFactory @Inject constructor(private val favouriteSongsDao: FavouriteSongsDao) {
    fun create(song: Song): SongPresenter = SongPresenter(favouriteSongsDao, song)
}