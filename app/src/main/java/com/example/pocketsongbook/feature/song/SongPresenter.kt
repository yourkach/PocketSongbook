package com.example.pocketsongbook.feature.song

import com.example.pocketsongbook.data.models.Chord
import com.example.pocketsongbook.data.models.SongModel
import com.example.pocketsongbook.common.BasePresenter
import com.example.pocketsongbook.common.BaseView
import com.example.pocketsongbook.common.extensions.setAndCancelJob
import com.example.pocketsongbook.domain.event_bus.Event
import com.example.pocketsongbook.domain.event_bus.SubscribeToEventsUseCase
import com.example.pocketsongbook.domain.favorites.AddToFavoritesUseCase
import com.example.pocketsongbook.domain.favorites.CheckIsFavoriteUseCase
import com.example.pocketsongbook.domain.favorites.RemoveFromFavoritesUseCase
import com.example.pocketsongbook.domain.song.EvaluateSongStateUseCase
import com.example.pocketsongbook.domain.song.SongViewState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import moxy.InjectViewState
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.AddToEndSingleTagStrategy
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

    @StateStrategyType(AddToEndSingleTagStrategy::class, tag = "chords_bar")
    fun openChordBar()

    @StateStrategyType(AddToEndSingleTagStrategy::class, tag = "chords_bar")
    fun closeChordBar()
}

enum class ChangeType {
    Increment, Decrement, SetDefault
}

@InjectViewState
class SongPresenter @AssistedInject constructor(
    @Assisted private val song: SongModel,
    private val addToFavoritesUseCase: AddToFavoritesUseCase,
    private val removeFromFavoritesUseCase: RemoveFromFavoritesUseCase,
    private val checkIsFavoriteUseCase: CheckIsFavoriteUseCase,
    private val subscribeToEventsUseCase: SubscribeToEventsUseCase,
    private val evaluateSongStateUseCase: EvaluateSongStateUseCase
) : BasePresenter<SongView>() {

    @AssistedFactory
    interface Factory {
        fun create(song: SongModel): SongPresenter
    }

    private var isFavourite: Boolean = false
        set(value) {
            field = value.also(viewState::setFavouritesButtonFilled)
        }

    private var currentFontSize: Int = FONT_SIZE_DEFAULT

    private var chordsBarOpened = false

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        viewState.setSongInfo(
            artist = song.artist,
            title = song.title
        )

        subscribeToEvents()
        checkForFavoriteStatus()
        updateLyrics()
    }

    private fun checkForFavoriteStatus() {
        launch {
            isFavourite = checkIsFavoriteUseCase(song)
        }
    }

    private fun subscribeToEvents() {
        launch {
            subscribeToEventsUseCase { event ->
                when (event) {
                    is Event.FavoritesChange -> {
                        if (event.url == song.url) isFavourite = event.isAdded
                    }
                }
            }
        }
    }

    private var changeKeyJob by setAndCancelJob()
    private var currentSongState: SongViewState? = null
    private fun updateLyrics(keyChangeType: ChangeType = ChangeType.SetDefault) {
        changeKeyJob = launch {
            val currentChordsKey = currentSongState?.chordsKey ?: 0
            val newKey = when (keyChangeType) {
                ChangeType.Increment -> (currentChordsKey + 1) % 12
                ChangeType.Decrement -> (currentChordsKey - 1) % 12
                ChangeType.SetDefault -> 0
            }
            currentSongState = evaluateSongStateUseCase(song, newKey).also { songState ->
                viewState.setKeyText(newKey.toString(), newKey == 0)
                viewState.setSongLyrics(songState.formattedLyricsText)
                if (chordsBarOpened) updateChordsImages(songState.chordsList)
            }
        }
    }

    private fun changeFontSize(changeType: ChangeType) {
        val newFontSize = when (changeType) {
            ChangeType.SetDefault -> {
                FONT_SIZE_DEFAULT
            }
            ChangeType.Increment,
            ChangeType.Decrement -> {
                currentFontSize + FONT_CHANGE_AMOUNT * if (changeType == ChangeType.Increment) 1 else -1
            }
        }
        currentFontSize = newFontSize.coerceIn(FONT_SIZE_MIN..FONT_SIZE_MAX)
        viewState.updateLyricsFontSize(
            newSize = currentFontSize,
            isDefault = currentFontSize == FONT_SIZE_DEFAULT
        )
    }

    fun onKeyPlusClick() {
        launch {
            updateLyrics(ChangeType.Increment)
        }
    }

    fun onKeyMinusClick() {
        launch {
            updateLyrics(ChangeType.Decrement)
        }
    }

    fun onKeyLabelClick() {
        launch {
            updateLyrics(ChangeType.SetDefault)
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
        launch {
            if (isFavourite) {
                removeFromFavoritesUseCase(song)
            } else {
                addToFavoritesUseCase(song)
            }
        }
    }

    fun onOpenChordsClick() {
        if (chordsBarOpened) {
            viewState.closeChordBar()
            chordsBarOpened = false
        } else {
            currentSongState?.let {
                viewState.openChordBar()
                updateChordsImages(it.chordsList)
                chordsBarOpened = true
            }
        }
    }

    private fun updateChordsImages(chordsList: List<String>) {
        val newChords = /*chordsList.map {
            Chord(
                it,
                "https://mychords.net/i/img/akkords/${it.replace("#", "sharp")}.png"
            )
        } + */chordsList.map {
            Chord(
                it,
                "https://amdm.ru/images/chords/${it.replace("#", "w")}_0.gif"
            )
        }
        viewState.setChords(newChords)
    }

    companion object {
        private const val FONT_CHANGE_AMOUNT: Int = 2
        const val FONT_SIZE_DEFAULT: Int = 16
        const val FONT_SIZE_MIN: Int = 8
        const val FONT_SIZE_MAX: Int = 36
    }
}
