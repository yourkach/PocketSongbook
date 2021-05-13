package com.example.pocketsongbook.domain.tuner

import com.example.pocketsongbook.domain.tuner.string_detection.GuitarString

sealed class TunerDetectionMode {
    abstract val string: GuitarString

    object AutoDetectString : TunerDetectionMode() {
        override val string: GuitarString = GuitarString.UNDEFINED
    }

    data class SelectedString(override val string: GuitarString) : TunerDetectionMode()
}