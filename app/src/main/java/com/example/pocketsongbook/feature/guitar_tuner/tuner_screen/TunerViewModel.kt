package com.example.pocketsongbook.feature.guitar_tuner.tuner_screen

import com.example.pocketsongbook.common.BaseViewModel
import com.example.pocketsongbook.common.extensions.alsoInvokeOnCompletion
import com.example.pocketsongbook.common.extensions.isNullOrCompleted
import com.example.pocketsongbook.common.extensions.setAndCancelJob
import com.example.pocketsongbook.domain.tuner.StringTuningResult
import com.example.pocketsongbook.domain.tuner.Tuner
import com.example.pocketsongbook.domain.tuner.TunerDetectionMode
import com.example.pocketsongbook.domain.tuner.string_detection.GuitarString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TunerViewModel @Inject constructor(
    private val tuner: Tuner
) : BaseViewModel() {

    private val mutableStateFlow by lazy { MutableStateFlow<TunerState>(TunerState.Inactive) }
    val viewStateFlow: StateFlow<TunerState> by lazy { mutableStateFlow }

    private val mutableActionsFlow by lazy { MutableSharedFlow<TunerViewAction>() }
    val viewActionsFlow: SharedFlow<TunerViewAction> by lazy { mutableActionsFlow }

    fun onRecordAudioPermissionDenied() {
        launch {
            mutableActionsFlow.emit(TunerViewAction.OpenPermissionsScreen)
        }
    }

    fun onToggleTunerClick() {
        if (detectionJob.isNullOrCompleted) {
            startDetectionWithMode(TunerDetectionMode.AutoDetectString)
        } else detectionJob = null
    }

    fun onAutoDetectStringClick() {
        startDetectionWithMode(TunerDetectionMode.AutoDetectString)
    }

    fun onStringButtonClick(string: GuitarString) {
        startDetectionWithMode(TunerDetectionMode.SelectedString(string))
    }

    fun onTabSwitchedAway() {
        detectionJob?.cancel()
    }

    private fun startDetectionWithMode(tunerMode: TunerDetectionMode) {
        if (detectionJob.isNullOrCompleted) {
            startDetection(tunerMode)
        } else {
            tuner.setDetectionMode(tunerMode)
        }
    }

    private var detectionJob by setAndCancelJob()
    private fun startDetection(mode: TunerDetectionMode) {
        detectionJob = launch {
            val mutableTunerState = MutableActiveTunerState(StringTuningResult.EmptyResult)
            withContext(Dispatchers.IO) {
                tuner.startListening(mode).collect { tuningResult ->
                    mutableTunerState.tuningResult = tuningResult
                    mutableStateFlow.emit(mutableTunerState)
                    mutableActionsFlow.emit(TunerViewAction.TunerStateUpdate)
                }
            }
        }.alsoInvokeOnCompletion {
            mutableStateFlow.value = TunerState.Inactive
        }
    }

}

