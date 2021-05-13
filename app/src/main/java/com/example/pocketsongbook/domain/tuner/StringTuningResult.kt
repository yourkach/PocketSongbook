package com.example.pocketsongbook.domain.tuner

import com.example.pocketsongbook.domain.tuner.string_detection.GuitarString

interface StringTuningResult {
    val string: GuitarString
    val frequency: Double
    val percentOffset: Double
    val isAutoDetectModeActive: Boolean

    object EmptyResult : StringTuningResult {
        override val string: GuitarString = GuitarString.UNDEFINED
        override val frequency: Double = 0.0
        override val percentOffset: Double = 0.0
        override val isAutoDetectModeActive: Boolean = true
    }
}

class MutableStringTuningResult(
    override var string: GuitarString,
    override var frequency: Double,
    override var percentOffset: Double,
    override var isAutoDetectModeActive: Boolean
) : StringTuningResult