package com.example.pocketsongbook.feature.guitar_tuner

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import com.example.pocketsongbook.common.BasePresenter
import com.example.pocketsongbook.common.BaseView
import com.example.pocketsongbook.feature.guitar_tuner.stream.AudioRecorderImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import moxy.InjectViewState
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType
import javax.inject.Inject
import javax.inject.Provider

interface TunerView : BaseView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setMessageText(text: String)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setFrequencyGraph(values: List<Pair<Float,Float>>)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun checkRecordAudioPermission()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun requestRecordAudioPermission()
}


@InjectViewState
class TunerPresenter @Inject constructor(
) : BasePresenter<TunerView>() {

    private val audioStreamProvider = AudioRecorderImpl()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.setMessageText("Test")
        viewState.checkRecordAudioPermission()
    }

    fun onRecordAudioPermissionGranted() {
        viewState.setMessageText("Thx for granted")
        launch {
            withContext(Dispatchers.IO) {
                val buffer = mutableListOf<Short>()
                audioStreamProvider.startRecording().buffer(20).collect {
                    if (buffer.size < 20000) {
                        buffer.addAll(it.asSequence())
                    } else {
                        onNewAudioBufferReceived(buffer)
                        buffer.clear()
                    }
                }
            }
        }
    }

    fun onStopClick() {
        audioStreamProvider.stop()
    }

    private suspend fun onNewAudioBufferReceived(data: List<Short>) {
        val converted = data.mapIndexed { index, value -> index.toFloat() to value.toFloat() }
        withContext(Dispatchers.Main) {
            viewState.setFrequencyGraph(converted)
        }
    }


    fun onRecordAudioPermissionDenied() {
        viewState.requestRecordAudioPermission()
    }

}


class AudioRecordProvider : Provider<AudioRecord> {

    private val sampleRateHz = 44100
    private val channelConfig = AudioFormat.CHANNEL_IN_MONO
    private val audioFormat = AudioFormat.ENCODING_PCM_16BIT


    override fun get(): AudioRecord {
        val minBufferSize = AudioRecord.getMinBufferSize(sampleRateHz, channelConfig, audioFormat)
        return AudioRecord(
            MediaRecorder.AudioSource.MIC,
            sampleRateHz,
            channelConfig,
            audioFormat,
            minBufferSize
        )
    }

}