package com.example.pocketsongbook.domain.tuner

import kotlinx.coroutines.flow.Flow


/**
 * An interface for all tuner implementations.
 */
interface Tuner {
    /**
     * Subscribes to the tuner for flow of guitar strings found as a result of a pitch detection algorithm.
     *
     * @return An [<] providing all the found [StringTuningResult].
     */
    fun startListening(mode: TunerDetectionMode): Flow<StringTuningResult>

    fun setDetectionMode(mode: TunerDetectionMode)

}