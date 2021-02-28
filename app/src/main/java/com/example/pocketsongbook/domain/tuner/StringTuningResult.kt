package com.example.pocketsongbook.domain.tuner

import com.example.pocketsongbook.domain.tuner.string_detection.GuitarString

interface StringTuningResult {
    val string: GuitarString
    val frequency: Double
    val percentOffset: Double
}

class MutableStringTuningResult(
    override var string: GuitarString,
    override var frequency: Double,
    override var percentOffset: Double
) : StringTuningResult