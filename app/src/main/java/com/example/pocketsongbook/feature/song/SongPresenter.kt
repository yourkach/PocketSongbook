package com.example.pocketsongbook.feature.song

import com.example.pocketsongbook.common.BasePresenter
import com.example.pocketsongbook.common.BaseView
import com.example.pocketsongbook.common.extensions.setAndCancelJob
import com.example.pocketsongbook.domain.event_bus.Event
import com.example.pocketsongbook.domain.event_bus.SubscribeToEventsUseCase
import com.example.pocketsongbook.domain.favorites.ToggleSongFavoriteStatusUseCase
import com.example.pocketsongbook.domain.models.SongModel
import com.example.pocketsongbook.domain.song_settings.usecase.SaveOrUpdateSongOptionsState
import com.example.pocketsongbook.feature.song.mvi.GetInitialSongStateUseCase
import com.example.pocketsongbook.feature.song.mvi.GetUpdatedStateUseCase
import com.example.pocketsongbook.feature.song.mvi.state_models.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import moxy.InjectViewState
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType


@StateStrategyType(AddToEndSingleStrategy::class)
interface SongView : BaseView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun applyState(state: SongScreenState)

}

@InjectViewState
class SongPresenter @AssistedInject constructor(
    @Assisted private val song: SongModel,
    private val subscribeToEventsUseCase: SubscribeToEventsUseCase,
    private val getInitialSongStateUseCase: GetInitialSongStateUseCase,
    private val getUpdatedStateUseCase: GetUpdatedStateUseCase,
    private val toggleSongFavoriteStatusUseCase: ToggleSongFavoriteStatusUseCase,
    private val saveOrUpdateSongOptionsState: SaveOrUpdateSongOptionsState
) : BasePresenter<SongView>() {

    @AssistedFactory
    interface Factory {
        fun create(song: SongModel): SongPresenter
    }

    private var currentScreenState: SongScreenState = SongScreenState(
        songState = SongViewStateModel.Loading,
        chordsBarState = ChordBarViewStateModel(
            isOpened = false,
            chords = listOf()
        )
    )
        set(value) {
            field = value.also(viewState::applyState)
        }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        subscribeToEvents()
        applyInitialState()
    }

    private fun applyInitialState() {
        launch {
            currentScreenState = getInitialSongStateUseCase(song)
        }
    }

    fun onSongToggleFavoriteClicked() {
        launch {
            toggleSongFavoriteStatusUseCase(song)
        }
    }

    private fun subscribeToEvents() {
        launch {
            subscribeToEventsUseCase { event ->
                if (event is Event.FavoritesChange && event.url == song.url) {
                    handleInteractionEvent(
                        interactionEvent = SongScreenInteractionEvent.FavoriteStatusChanged(
                            isFavorite = event.isAdded
                        )
                    )
                }
            }
        }
    }

    private var handleInteractionJob by setAndCancelJob()
    private fun handleInteractionEvent(interactionEvent: SongScreenInteractionEvent) {
        handleInteractionJob = launch {
            currentScreenState = getUpdatedStateUseCase(currentScreenState, interactionEvent)
        }
    }

    fun onChordsBarButtonClick() {
        handleInteractionEvent(SongScreenInteractionEvent.ChordsBarButtonClick)
    }

    fun onFontSizeChangeInteraction(changeType: ChangeType) {
        handleInteractionEvent(SongScreenInteractionEvent.ChangeFontSize(changeType))
    }

    fun onChordsKeyChangeInteraction(changeType: ChangeType) {
        handleInteractionEvent(SongScreenInteractionEvent.ChangeChordsKey(changeType))
    }

    fun onViewPaused() {
        GlobalScope.launch {
            (currentScreenState.songState as? SongViewStateModel.Loaded)?.let {
                if (it.isFavorite) {
                    saveOrUpdateSongOptionsState(
                        songUrl = it.songUrl,
                        optionsState = it.optionsState
                    )
                }
            }
        }
    }

}
