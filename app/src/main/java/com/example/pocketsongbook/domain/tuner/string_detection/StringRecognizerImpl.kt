package com.example.pocketsongbook.domain.tuner.string_detection

import toothpick.InjectConstructor
import kotlin.math.absoluteValue

@InjectConstructor
class StringRecognizerImpl : StringRecognizer {

    private var stringToPitch: List<Pair<GuitarString, Double>> = listOf(
        GuitarString.UNDEFINED to 350.0,
        GuitarString.E_1 to 329.5,
        GuitarString.B_2 to 246.8,
        GuitarString.G_3 to 195.5,
        GuitarString.D_4 to 146.5,
        GuitarString.A_5 to 109.7,
        GuitarString.E_6 to 82.1,
        GuitarString.UNDEFINED to 0.0,
    ).sortedBy { it.second }

    @Synchronized
    override fun setFrequency(frequency: Double) {
        string = GuitarString.UNDEFINED
        stringToPitch.minByOrNull { (it.second - frequency).absoluteValue }
            ?.let { (guitarString, stringPitch) ->
                string = guitarString
                percentageDifference = ((frequency - stringPitch) / 25.0).coerceIn(-1.0, 1.0)
            }
    }

    override var string: GuitarString = GuitarString.UNDEFINED
        private set
    override var percentageDifference: Double = 0.0
        private set
}