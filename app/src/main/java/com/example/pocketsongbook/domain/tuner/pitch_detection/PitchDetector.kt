package com.example.pocketsongbook.domain.tuner.pitch_detection

/**
 * An interface for implementations that detect a pitch using an array representation of a
 * sinusoidal sound wave.
 */
interface PitchDetector {
    /**
     * Retrieves a pitch frequency (note) using the provided array of values representing a sound
     * wave. Note that this method operates on the calling Thread. The calling Thread should be off
     * the main Thread.
     *
     * @param wave The array of 16 bit values representing the sound wave to be processed.
     * @return A double value representing the detected pitch.
     */
    fun detect(wave: FloatArray): Double
}