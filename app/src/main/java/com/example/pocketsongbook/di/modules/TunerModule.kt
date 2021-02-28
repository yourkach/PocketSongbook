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
import dagger.Binds
import dagger.Module

@Module
interface TunerModule {

    @Binds
    fun bindAudioConfig(impl: AudioConfigImpl): AudioConfig

    @Binds
    fun bindArrayConverter(pcmArrayConverter: PCMArrayConverter): ArrayConverter

    @Binds
    fun bindPitchDetector(impl: PitchDetectorImpl): PitchDetector

    @Binds
    fun bindStringRecognizer(impl: StringRecognizerImpl): StringRecognizer

    @Binds
    fun bindAudioRecorder(impl: AudioRecorderImpl): AudioRecorder

    @Binds
    fun bindTuner(impl: TunerImpl): Tuner

//    @Binds
//    fun bindTuner(impl:TunerImpl) : Tuner

}