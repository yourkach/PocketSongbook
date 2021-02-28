package com.example.pocketsongbook.domain.tuner.converter

/**
 * An interface for a class that converters byte or short arrays to a float array representing the
 * same values.
 */
interface ArrayConverter {
    /**
     * Converts the provided byte array to a corresponding float array.
     *
     * @param array          The byte array to be converted.
     * @param convertedArray The float array representing the original byte array but as float values.
     */
    fun convert(array: ByteArray, convertedArray: FloatArray)

    /**
     * Converts the provided short array to a corresponding float array.
     *
     * @param array          The short array to be converted.
     * @param convertedArray The float array representing the original short array but as float values.
     */
    fun convert(array: ShortArray, convertedArray: FloatArray)
}