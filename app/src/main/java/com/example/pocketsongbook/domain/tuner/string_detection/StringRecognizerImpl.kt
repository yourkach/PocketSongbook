package com.example.pocketsongbook.domain.tuner.string_detection

import com.example.pocketsongbook.domain.tuner.TunerDetectionMode
import javax.inject.Inject
import kotlin.math.absoluteValue

class StringRecognizerImpl @Inject constructor() : StringRecognizer {

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

    override fun setFrequency(frequency: Double) {
        if (frequency == -1.0) {
            string = stringDetectMode.string
            percentageDifference = 0.0
            return
        }
        (stringDetectMode as? TunerDetectionMode.SelectedString)?.let { detectMode ->
            string = detectMode.string
            val stringPitch =
                stringToPitch.first { (string, _) -> string == detectMode.string }.second
            percentageDifference = ((frequency - stringPitch) / 25.0).coerceIn(-1.0, 1.0)
        } ?: run {
            string = GuitarString.UNDEFINED
            stringToPitch.minByOrNull { (it.second - frequency).absoluteValue }
                ?.let { (guitarString, stringPitch) ->
                    string = guitarString
                    percentageDifference = ((frequency - stringPitch) / 25.0).coerceIn(-1.0, 1.0)
                }
        }
    }

    override var stringDetectMode: TunerDetectionMode = TunerDetectionMode.AutoDetectString

    override var string: GuitarString = GuitarString.UNDEFINED
        private set
    override var percentageDifference: Double = 0.0
        private set
}