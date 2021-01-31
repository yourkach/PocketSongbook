package com.example.pocketsongbook.feature.guitar_tuner.stream

class AudioPitchDetectorImpl : AudioPitchDetector {
    override fun getPitch(
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
}