package com.example.pocketsongbook.feature.guitar_tuner.stream

interface AudioPitchDetector {
    fun getPitch(
        data: ShortArray,
        windowSize: Int,
        frames: Int,
        sampleRate: Float,
        minFreq: Float,
        maxFreq: Float
    ): Float
}