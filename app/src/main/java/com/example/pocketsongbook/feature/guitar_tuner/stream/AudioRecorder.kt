package com.example.pocketsongbook.feature.guitar_tuner.stream

import kotlinx.coroutines.flow.Flow

interface AudioRecorder {
    fun startRecording(): Flow<ShortArray>
    fun stop()
}