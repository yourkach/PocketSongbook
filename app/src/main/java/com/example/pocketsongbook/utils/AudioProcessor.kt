package com.example.pocketsongbook.utils

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.util.Log
import kotlin.math.absoluteValue

/*
* Copyright 2016 andryr
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/ /**
 * Created by andry on 24/04/16.
 */
class AudioProcessor : Runnable {
    interface PitchDetectionListener {
        fun onPitchDetected(freq: Float, avgIntensity: Double)
    }

    private var mLastComputedFreq = 0f
    private var mAudioRecord: AudioRecord? = null
    private var mPitchDetectionListener: PitchDetectionListener? = null
    private var mStop = false
    fun setPitchDetectionListener(pitchDetectionListener: PitchDetectionListener?) {
        mPitchDetectionListener = pitchDetectionListener
    }

    fun init() {
        val bufSize = 16384
        val avalaibleSampleRates = SAMPLE_RATES.size
        var i = 0
        do {
            val sampleRate = SAMPLE_RATES[i]
            val minBufSize = AudioRecord.getMinBufferSize(
                sampleRate,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT
            )
            if (minBufSize != AudioRecord.ERROR_BAD_VALUE && minBufSize != AudioRecord.ERROR) {
                mAudioRecord = AudioRecord(
                    MediaRecorder.AudioSource.MIC,
                    sampleRate,
                    AudioFormat.CHANNEL_IN_MONO,
                    AudioFormat.ENCODING_PCM_16BIT,
                    Math.max(bufSize, minBufSize * 4)
                )
            }
            i++
        } while (i < avalaibleSampleRates && (mAudioRecord == null || mAudioRecord!!.state != AudioRecord.STATE_INITIALIZED))
    }

    fun stop() {
        mStop = true
        mAudioRecord!!.stop()
        mAudioRecord!!.release()
    }

    override fun run() {
        Log.d(TAG, "sampleRate=" + mAudioRecord!!.sampleRate)
        if (mAudioRecord!!.state != AudioRecord.STATE_INITIALIZED) {
            Log.e(TAG, "AudioRecord not initialized")
        }
        mAudioRecord!!.startRecording()
        val bufSize = 8192
        val sampleRate = mAudioRecord!!.sampleRate
        val buffer = ShortArray(bufSize)
        do {
            val read = mAudioRecord!!.read(buffer, 0, bufSize)
            if (read > 0) {
                val intensity = averageIntensity(buffer, read)
                val maxZeroCrossing = (250 * (read / 8192) * (sampleRate / 44100.0)).toInt()
                if (intensity >= 50 && zeroCrossingCount(buffer) <= maxZeroCrossing) {
                    val freq = getPitch(
                        data = buffer,
                        windowSize = read / 4,
                        frames = read,
                        sampleRate = sampleRate.toFloat(),
                        minFreq = 50f,
                        maxFreq = 500f
                    )
                    if (Math.abs(freq - mLastComputedFreq) <= 5f) {
                        mPitchDetectionListener!!.onPitchDetected(freq, intensity)
                    }
                    mLastComputedFreq = freq
                }
            }
        } while (!mStop)
        Log.d(TAG, "Thread terminated")
    }

    private fun averageIntensity(data: ShortArray, frames: Int): Double {
        var sum = 0.0
        for (i in 0 until frames) {
            sum += data[i].toDouble().absoluteValue
        }
        return sum / frames
    }

    private fun zeroCrossingCount(data: ShortArray): Int {
        val len = data.size
        var count = 0
        var prevValPositive = data[0] >= 0
        for (i in 1 until len) {
            val positive = data[i] >= 0
            if (prevValPositive == !positive) count++
            prevValPositive = positive
        }
        return count
    }

    private fun getPitch(
        data: ShortArray,
        windowSize: Int,
        frames: Int,
        sampleRate: Float,
        minFreq: Float,
        maxFreq: Float
    ): Float {
        val maxOffset = sampleRate / minFreq
        val minOffset = sampleRate / maxFreq
        var minSum = Int.MAX_VALUE
        var minSumLag = 0
        val sums = IntArray(Math.round(maxOffset) + 2)
        var lag = minOffset.toInt()
        while (lag <= maxOffset) {
            var sum = 0
            for (i in 0 until windowSize) {
                val oldIndex = i - lag
                val sample = (if (oldIndex < 0) data[frames + oldIndex] else data[oldIndex]).toInt()
                sum += Math.abs(sample - data[i])
            }
            sums[lag] = sum
            if (sum < minSum) {
                minSum = sum
                minSumLag = lag
            }
            lag++
        }

        // quadratic interpolation
        val delta =
            (sums[minSumLag + 1] - sums[minSumLag - 1]).toFloat() / (2 * (2 * sums[minSumLag] - sums[minSumLag + 1] - sums[minSumLag - 1])).toFloat()
        return sampleRate / (minSumLag + delta)
    }

    companion object {
        private val TAG = this::class.java.simpleName
        private val SAMPLE_RATES = intArrayOf(44100, 22050, 16000, 11025, 8000)
    }
}