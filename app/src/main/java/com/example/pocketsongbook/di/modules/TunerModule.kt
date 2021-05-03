package com.example.pocketsongbook.di.modules

import com.example.pocketsongbook.domain.tuner.Tuner
import com.example.pocketsongbook.domain.tuner.TunerImpl
import com.example.pocketsongbook.domain.tuner.config.AudioConfig
import com.example.pocketsongbook.domain.tuner.config.AudioConfigImpl
import com.example.pocketsongbook.domain.tuner.converter.ArrayConverter
import com.example.pocketsongbook.domain.tuner.converter.PCMArrayConverter
import com.example.pocketsongbook.domain.tuner.pitch_detection.PitchDetector
import com.example.pocketsongbook.domain.tuner.pitch_detection.PitchDetectorImpl
import com.example.pocketsongbook.domain.tuner.recorder.AudioRecorder
import com.example.pocketsongbook.domain.tuner.recorder.AudioRecorderImpl
import com.example.pocketsongbook.domain.tuner.string_detection.StringRecognizer
import com.example.pocketsongbook.domain.tuner.string_detection.StringRecognizerImpl
import toothpick.config.Module

class TunerModule : Module() {
    init {
        bind(AudioConfig::class.java).to(AudioConfigImpl::class.java)
        bind(ArrayConverter::class.java).to(PCMArrayConverter::class.java)
        bind(PitchDetector::class.java).to(PitchDetectorImpl::class.java)
        bind(StringRecognizer::class.java).to(StringRecognizerImpl::class.java)
        bind(AudioRecorder::class.java).to(AudioRecorderImpl::class.java)
        bind(Tuner::class.java).to(TunerImpl::class.java)
    }
}

