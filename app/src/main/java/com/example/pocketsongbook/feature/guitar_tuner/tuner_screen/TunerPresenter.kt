package com.example.pocketsongbook.feature.guitar_tuner.tuner_screen

import com.example.pocketsongbook.common.BasePresenter
import com.example.pocketsongbook.common.BaseView
import com.example.pocketsongbook.common.extensions.alsoInvokeOnCompletion
import com.example.pocketsongbook.common.extensions.isNullOrCompleted
import com.example.pocketsongbook.common.extensions.setAndCancelJob
import com.example.pocketsongbook.domain.tuner.StringTuningResult
import com.example.pocketsongbook.domain.tuner.Tuner
import com.example.pocketsongbook.domain.tuner.TunerDetectionMode
import com.example.pocketsongbook.domain.tuner.string_detection.GuitarString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import moxy.InjectViewState
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType
import javax.inject.Inject

interface TunerView : BaseView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun updateTunerState(tunerState: TunerState)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun toMicroPermissionScreen()
}


@InjectViewState
class TunerPresenter @Inject constructor(
    private val tuner: Tuner
) : BasePresenter<TunerView>() {

    fun onRecordAudioPermissionDenied() {
        viewState.toMicroPermissionScreen()
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
                    withContext(Dispatchers.Main) {
                        mutableTunerState.tuningResult = tuningResult
                        viewState.updateTunerState(mutableTunerState)
                    }
                }
            }
        }.alsoInvokeOnCompletion {
            viewState.updateTunerState(TunerState.Inactive)
        }
    }

    fun onTabSwitchedAway() {
        detectionJob?.cancel()
    }

}
