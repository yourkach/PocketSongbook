package com.example.pocketsongbook.domain.tuner.string_detection

import com.example.pocketsongbook.domain.tuner.TunerDetectionMode

/**
 * An interface for implementations that finds the closest string and frequency difference
 * based on a provided frequency value.
 */
interface StringRecognizer {

    /**
     * Sets the frequency of the note. This method must be called before calling the other methods.
     * Failure to do so may result in unspecified behavior.
     *
     * @param frequency The frequency of the string.
     */
    fun setFrequency(frequency: Double)

    /**
     * Retrieves the closest guitar string to the provided frequency.
     */
    val string: GuitarString

    /**
     * Retrieves the percent difference between the closest found string and the provided frequency.
     * It is an offset in percentage between the closest note and the second closest note. A
     * negative value will indicate it is a lower pitch than the closest found string and a positive
     * value will indicate it is a higher pitch than the closest found string.
     *
     * @return The percent offset from the closest string, in range [-1, 1]
     */
    val percentageDifference: Double

    var stringDetectMode: TunerDetectionMode
}