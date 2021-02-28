package com.example.pocketsongbook.domain.tuner.config

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.AudioTrack
import android.media.MediaRecorder
import android.os.Build
import javax.inject.Inject

class AudioConfigImpl @Inject constructor() : AudioConfig {

    override val sampleRate: Int
        get() = AUDIO_SAMPLE_RATE
    override val inputBufferSize: Int
        get() = AUDIO_RECORD_BUFFER_SIZE
    override val outputBufferSize: Int
        get() = AUDIO_PLAYER_BUFFER_SIZE
    override val readSize: Int
        get() = AUDIO_RECORD_READ_SIZE
    override val writeSize: Int
        get() = AUDIO_PLAYER_WRITE_SIZE
    override val inputChannel: Int
        get() = AUDIO_RECORD_CHANNEL_CONFIG
    override val outputChannel: Int
        get() = AUDIO_PLAYER_CHANNEL_CONFIG
    override val inputFormat: Int
        get() = AUDIO_RECORD_AUDIO_FORMAT
    override val outputFormat: Int
        get() = AUDIO_PLAYER_AUDIO_FORMAT
    override val outputFormatByteCount: Int
        get() = AUDIO_PLAYER_AUDIO_FORMAT_BYTE_COUNT
    override val inputSource: Int
        get() = AUDIO_RECORD_AUDIO_SOURCE

    companion object {
        private const val AUDIO_SAMPLE_RATE: Int = 44100

        private const val AUDIO_RECORD_CHANNEL_CONFIG: Int = AudioFormat.CHANNEL_IN_DEFAULT

        private val AUDIO_RECORD_AUDIO_FORMAT: Int =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                AudioFormat.ENCODING_PCM_FLOAT
            } else {
                AudioFormat.ENCODING_PCM_16BIT
            }
        private val AUDIO_RECORD_BUFFER_SIZE: Int = AudioRecord.getMinBufferSize(
            AUDIO_SAMPLE_RATE,
            AUDIO_RECORD_CHANNEL_CONFIG,
            AUDIO_RECORD_AUDIO_FORMAT
        )
        private val AUDIO_RECORD_READ_SIZE: Int = AUDIO_RECORD_BUFFER_SIZE / 4

        private const val AUDIO_RECORD_AUDIO_SOURCE: Int = MediaRecorder.AudioSource.DEFAULT

        private const val AUDIO_PLAYER_CHANNEL_CONFIG: Int = AudioFormat.CHANNEL_OUT_STEREO

        private const val AUDIO_PLAYER_AUDIO_FORMAT: Int = AudioFormat.ENCODING_PCM_FLOAT

        private const val AUDIO_PLAYER_AUDIO_FORMAT_BYTE_COUNT: Int = 2

        private val AUDIO_PLAYER_BUFFER_SIZE: Int = AudioTrack.getMinBufferSize(
            AUDIO_SAMPLE_RATE,
            AUDIO_PLAYER_CHANNEL_CONFIG,
            AUDIO_PLAYER_AUDIO_FORMAT
        )

        private val AUDIO_PLAYER_WRITE_SIZE: Int = AUDIO_PLAYER_BUFFER_SIZE / 4
    }
}