package com.example.pocketsongbook.domain.tuner.string_detection

/**
 * An interface providing a way to obtain a frequency from a given note name.
 */
interface StringFrequencyRetriever {
    /**
     * Retrieves a frequency for the provided note name.
     *
     * @param string The string whose frequency is being retrieved.
     * @return The frequency representing the provided note name.
     */
    fun getFrequency(string: GuitarString): Double
}