package com.example.pocketsongbook.domain.tuner

import com.example.pocketsongbook.domain.tuner.pitch_detection.PitchDetector
import com.example.pocketsongbook.domain.tuner.recorder.AudioRecorder
import com.example.pocketsongbook.domain.tuner.string_detection.GuitarString
import com.example.pocketsongbook.domain.tuner.string_detection.StringRecognizer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

/**
 * An implementation of the [Tuner] interface.
 */
class TunerImpl @Inject constructor(
    private val audioRecorder: AudioRecorder,
    private val detector: PitchDetector,
    private val stringRecognizer: StringRecognizer
) : Tuner {

    private val tuningResult = MutableStringTuningResult(
        string = GuitarString.UNDEFINED,
        frequency = 0.0,
        percentOffset = 0.0,
        isAutoDetectModeActive = true
    )

    override fun setDetectionMode(mode: TunerDetectionMode) {
        stringRecognizer.stringDetectMode = mode
    }

    override fun startListening(mode: TunerDetectionMode): Flow<StringTuningResult> {
        return flow {
            try {
                Timber.d("Started")
                stringRecognizer.stringDetectMode = mode
                audioRecorder.startRecording()
                while (true) {
                    val audioWave = audioRecorder.readNext()
                    val frequency = detector.detect(audioWave)
                    tuningResult.updateByFrequency(frequency)
                    emit(tuningResult)
                }
            } catch (e: Throwable) {
                Timber.e(e)
            } finally {
                Timber.d("Finished")
                audioRecorder.stopRecording()
            }
        }
    }

    private fun MutableStringTuningResult.updateByFrequency(frequency: Double) {
        stringRecognizer.setFrequency(frequency)
        tuningResult.frequency = frequency
        tuningResult.string = stringRecognizer.string
        tuningResult.percentOffset = stringRecognizer.percentageDifference
        tuningResult.isAutoDetectModeActive =
            stringRecognizer.stringDetectMode is TunerDetectionMode.AutoDetectString
    }

}