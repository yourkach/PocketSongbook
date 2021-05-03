package com.example.pocketsongbook.domain.tuner

import com.example.pocketsongbook.domain.tuner.pitch_detection.PitchDetector
import com.example.pocketsongbook.domain.tuner.recorder.AudioRecorder
import com.example.pocketsongbook.domain.tuner.string_detection.GuitarString
import com.example.pocketsongbook.domain.tuner.string_detection.StringRecognizer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import toothpick.InjectConstructor

/**
 * An implementation of the [Tuner] interface.
 */
@InjectConstructor
class TunerImpl(
    private val audioRecorder: AudioRecorder,
    private val detector: PitchDetector,
    private val stringRecognizer: StringRecognizer
) : Tuner {

    private val tuningString = MutableStringTuningResult(
        string = GuitarString.UNDEFINED,
        frequency = 0.0,
        percentOffset = 0.0
    )

    override fun startListening(): Flow<StringTuningResult> {
        return flow {
            try {
                Timber.d("Started")
                audioRecorder.startRecording()
                while (true) {
                    val pitchFrequency: Double = detector.detect(audioRecorder.readNext())
                    stringRecognizer.setFrequency(pitchFrequency)
                    synchronized(tuningString) {
                        tuningString.frequency = pitchFrequency
                        tuningString.string = stringRecognizer.string
                        tuningString.percentOffset = stringRecognizer.percentageDifference
                    }
                    this.emit(tuningString)
                }
            } catch (e: Throwable) {
                Timber.e(e)
            } finally {
                Timber.d("Finished")
                audioRecorder.stopRecording()
            }
        }
    }

}