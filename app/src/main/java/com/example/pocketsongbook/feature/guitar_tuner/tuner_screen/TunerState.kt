package com.example.pocketsongbook.feature.guitar_tuner.tuner_screen

import com.example.pocketsongbook.domain.tuner.StringTuningResult

sealed class TunerState {

    object Inactive : TunerState()

    abstract class Active : TunerState() {
        abstract val tuningResult: StringTuningResult
    }

}

val TunerState.isTunerActive: Boolean
    get() = this is TunerState.Active

class MutableActiveTunerState(
    override var tuningResult: StringTuningResult
) : TunerState.Active()