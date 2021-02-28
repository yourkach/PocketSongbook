package com.example.pocketsongbook.domain.tuner.recorder

import android.media.AudioRecord
import android.os.Build
import com.example.pocketsongbook.domain.tuner.config.AudioConfig
import com.example.pocketsongbook.domain.tuner.converter.ArrayConverter
import javax.inject.Inject

/**
 * An Android implementation of the [AudioRecorder] interface that works with the Android
 * [AudioRecord] object.
 */
class AudioRecorderImpl @Inject constructor(
    private val converter: ArrayConverter,
    audioConfig: AudioConfig
) : AudioRecorder {
    private val audioRecorder: AudioRecord = AudioRecord(
        audioConfig.inputSource,
        audioConfig.sampleRate,
        audioConfig.inputChannel,
        audioConfig.inputFormat,
        audioConfig.inputBufferSize
    )
    private val readSize: Int = audioConfig.readSize
    private val buffer: ShortArray = ShortArray(readSize)
    private val floatBuffer: FloatArray = FloatArray(readSize)
    override fun startRecording() {
        audioRecorder.startRecording()
    }

    override fun stopRecording() {
        audioRecorder.stop()
    }

    override fun readNext(): FloatArray {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            audioRecorder.read(floatBuffer, 0, readSize, AudioRecord.READ_BLOCKING)
        } else {
            audioRecorder.read(buffer, 0, readSize)
            converter.convert(buffer, floatBuffer)
        }
        return floatBuffer
    }
}