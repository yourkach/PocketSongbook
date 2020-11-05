package com.example.pocketsongbook.feature.song

import com.example.pocketsongbook.data.database.FavouriteSongsDao
import com.example.pocketsongbook.data.models.Chord
import com.example.pocketsongbook.data.models.Song
import com.example.pocketsongbook.data.database.SongEntity
import com.example.pocketsongbook.common.BasePresenter
import com.example.pocketsongbook.common.BaseView
import com.example.pocketsongbook.data.favourites.FavouriteSongsRepo
import com.example.pocketsongbook.feature.song.domain.*
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import moxy.InjectViewState
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType


@StateStrategyType(AddToEndSingleStrategy::class)
interface SongView : BaseView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setSongInfo(artist: String, title: String)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setSongLyrics(lyricsHtml: String)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setKeyText(chordsKeyText: String, isDefault: Boolean)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun updateLyricsFontSize(newSize: Int, isDefault: Boolean = false)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setFavouritesButtonFilled(filled: Boolean)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setChords(chords: List<Chord>)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun openChordBar()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun closeChordBar()
}

enum class ChangeType {
    Increment, Decrement, SetDefault
}

@InjectViewState
class SongPresenter @AssistedInject constructor(
    @Assisted private val song: Song,
    private val favouriteSongsRepo: FavouriteSongsRepo
) : BasePresenter<SongView>(), SongHolder.SongChangesListener {

    private val songHolder = SongHolder(song)

    private var isFavourite: Boolean = false
        set(value) {
            field = value
            viewState.setFavouritesButtonFilled(value)
        }
    private var currentFontSize: Float =
        FONT_SIZE_DEFAULT
    private var chordsBarOpened = false

    override fun onSongStateChanged(newState: SongHolder.SongState) {
        viewState.setSongLyrics(lyricsHtml = newState.formattedSongHtmlText)
        viewState.setKeyText(
            chordsKeyText = newState.chordsKey.toString(),
            isDefault = newState.chordsKey == 0
        )
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        viewState.setSongInfo(
            artist = song.artist,
            title = song.title
        )

        songHolder.subscribe(listener = this)

        launch {
            withContext(Dispatchers.IO) {
                isFavourite = favouriteSongsRepo.containsSong(song.url)
            }
        }
    }

    private fun changeFontSize(changeType: ChangeType) {
        val newFontSize = when (changeType) {
            ChangeType.SetDefault -> {
                FONT_SIZE_DEFAULT
            }
            ChangeType.Increment, ChangeType.Decrement -> {
                currentFontSize + FONT_CHANGE_AMOUNT * if (changeType == ChangeType.Increment) 1 else -1
            }
        }
        currentFontSize = newFontSize.coerceIn(FONT_SIZE_MIN..FONT_SIZE_MAX)
        viewState.updateLyricsFontSize(
            currentFontSize.toInt(),
            currentFontSize == FONT_SIZE_DEFAULT
        )
    }

    fun onKeyPlusClick() {
        launch {
            songHolder.changeChordsKey(ChangeType.Increment)
        }
    }

    fun onKeyMinusClick() {
        launch {
            songHolder.changeChordsKey(ChangeType.Decrement)
        }
    }

    fun onKeyLabelClick() {
        launch {
            songHolder.changeChordsKey(ChangeType.SetDefault)
        }
    }

    fun onFontPlusClick() {
        changeFontSize(ChangeType.Increment)
    }

    fun onFontMinusClick() {
        changeFontSize(ChangeType.Decrement)
    }

    fun onFontLabelClick() {
        changeFontSize(ChangeType.SetDefault)
    }

    fun onFavouritesButtonClick() {
        if (isFavourite) {
            removeFromFavourites()
        } else {
            addToFavourites()
        }
    }

    fun onOpenChordsClick() {
        if (chordsBarOpened) {
            viewState.closeChordBar()
        } else {
            viewState.openChordBar()
            updateChordsImages()
        }
        chordsBarOpened = !chordsBarOpened
    }

    // TODO: 05.11.20 вынести в юзкейсы
    private fun addToFavourites() {
        launch {
            withContext(Dispatchers.IO) {
                favouriteSongsRepo.addSong(song)
            }
        }
        isFavourite = true
    }

    private fun removeFromFavourites() {
        launch {
            withContext(Dispatchers.IO) {
                favouriteSongsRepo.removeSong(song)
            }
        }
        isFavourite = false
    }


    private fun updateChordsImages() {
        val newChords = songHolder.currentState.chordsList.map {
            Chord(
                it,
                "https://mychords.net/i/img/akkords/${it.replace("#", "sharp")}.png"
            )
        } + songHolder.currentState.chordsList.map {
            Chord(
                it,
                "https://amdm.ru/images/chords/${it.replace("#", "w")}_0.gif"
            )
        }
        viewState.setChords(newChords)
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(song: Song): SongPresenter
    }

    companion object {
        private const val FONT_CHANGE_AMOUNT: Float = 2.0F
        const val FONT_SIZE_DEFAULT: Float = 16.0F
        const val FONT_SIZE_MIN: Float = 8.0F
        const val FONT_SIZE_MAX: Float = 36.0F
    }
}
