package com.example.pocketsongbook.feature.guitar_tuner.stream

import com.example.pocketsongbook.feature.guitar_tuner.AudioRecordProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.yield

class AudioRecorderImpl : AudioRecorder {

    private val recordProvider = AudioRecordProvider()

    @Volatile
    private var isStopped = false

    override fun startRecording(): Flow<ShortArray> {
        return flow {
            val audioRecord = recordProvider.get()
            val buffer = ShortArray(1024)
            try {
                audioRecord.startRecording()
                while (!isStopped) {
                    yield()
                    val readSize = audioRecord.read(buffer, 0, buffer.size)
                    emit(buffer.copyOfRange(0, readSize))
                }
            } finally {
                audioRecord.stop()
                audioRecord.release()
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun stop() {
        isStopped = true
    }
}