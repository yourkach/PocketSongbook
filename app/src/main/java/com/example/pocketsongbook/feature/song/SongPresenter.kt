package com.example.pocketsongbook.feature.song

import com.example.pocketsongbook.data.models.Chord
import com.example.pocketsongbook.data.models.SongModel
import com.example.pocketsongbook.common.BasePresenter
import com.example.pocketsongbook.common.BaseView
import com.example.pocketsongbook.domain.event_bus.Event
import com.example.pocketsongbook.domain.event_bus.SubscribeToEventsUseCase
import com.example.pocketsongbook.domain.favorites.AddToFavoritesUseCase
import com.example.pocketsongbook.domain.favorites.CheckIsFavoriteUseCase
import com.example.pocketsongbook.domain.favorites.RemoveFromFavoritesUseCase
import com.example.pocketsongbook.feature.song.domain.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
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
    @Assisted private val song: SongModel,
    private val addToFavoritesUseCase: AddToFavoritesUseCase,
    private val removeFromFavoritesUseCase: RemoveFromFavoritesUseCase,
    private val checkIsFavoriteUseCase: CheckIsFavoriteUseCase,
    private val subscribeToEventsUseCase: SubscribeToEventsUseCase
) : BasePresenter<SongView>() {

    @AssistedFactory
    interface Factory {
        fun create(song: SongModel): SongPresenter
    }

    private val songHolder = SongHolder(
        song = song,
        onSongStateChanged = ::onSongStateChanged
    )

    private var isFavourite: Boolean = false
        set(value) {
            field = value
            viewState.setFavouritesButtonFilled(value)
        }

    private var currentFontSize: Int = FONT_SIZE_DEFAULT

    private var chordsBarOpened = false

    private fun onSongStateChanged(newState: SongHolder.SongState) {
        viewState.setSongLyrics(lyricsHtml = newState.formattedHtmlLyricsText)
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

        subscribeToEvents()

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

    private fun addToFavourites() {
        launch {
            addToFavoritesUseCase(song)
            isFavourite = true
        }
    }

    private fun removeFromFavourites() {
        launch {
            removeFromFavoritesUseCase(song)
            isFavourite = false
        }
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

    companion object {
        private const val FONT_CHANGE_AMOUNT: Int = 2
        const val FONT_SIZE_DEFAULT: Int = 16
        const val FONT_SIZE_MIN: Int = 8
        const val FONT_SIZE_MAX: Int = 36
    }
}
