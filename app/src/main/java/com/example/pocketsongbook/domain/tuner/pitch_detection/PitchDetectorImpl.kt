package com.example.pocketsongbook.domain.tuner.pitch_detection

import be.tarsos.dsp.pitch.Yin
import com.example.pocketsongbook.domain.tuner.config.AudioConfig
import toothpick.InjectConstructor

/**
 * Adapter for [Yin] pitch detector from TarsosDSP library
 */
@InjectConstructor
class PitchDetectorImpl(
    audioConfig: AudioConfig
) : PitchDetector {

    private val yinDetector = Yin(audioConfig.sampleRate.toFloat(), audioConfig.readSize)

    override fun detect(wave: FloatArray): Double {
        return yinDetector.getPitch(wave).pitch.toDouble()
    }

}