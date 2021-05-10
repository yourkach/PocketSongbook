package com.example.pocketsongbook.feature.guitar_tuner.tuner_screen

import com.example.pocketsongbook.common.BasePresenter
import com.example.pocketsongbook.common.BaseView
import com.example.pocketsongbook.common.extensions.setAndCancelJob
import com.example.pocketsongbook.domain.tuner.StringTuningResult
import com.example.pocketsongbook.domain.tuner.Tuner
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
    fun updateTunerResult(tuningResult: StringTuningResult)

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

    fun onStartClick() {
        startDetection()
    }

    private var detectionJob by setAndCancelJob()
    private fun startDetection() {
        detectionJob = launch {
            withContext(Dispatchers.IO) {
                tuner.startListening().collect { tuningResult ->
                    withContext(Dispatchers.Main) {
                        viewState.updateTunerResult(tuningResult)
                    }
                }
            }
        }
    }

    fun onStopClick() {
        detectionJob = null
    }

}