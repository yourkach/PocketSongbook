package com.example.pocketsongbook.feature.guitar_tuner

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.example.pocketsongbook.R
import com.example.pocketsongbook.common.BaseFragment
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.android.synthetic.main.fragment_tuner.*
import moxy.ktx.moxyPresenter
import javax.inject.Inject
import javax.inject.Provider


class TunerFragment : BaseFragment(R.layout.fragment_tuner), TunerView {

    @Inject
    lateinit var presenterProvider: Provider<TunerPresenter>

    private val presenter by moxyPresenter {
        presenterProvider.get()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnStop.setOnClickListener {
            presenter.onStopClick()
        }
        setUpChart()
    }

    private fun setUpChart() {
        chartView.apply {

        }
    }

    override fun checkRecordAudioPermission() {
        val resultCode = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.RECORD_AUDIO
        )
        when (resultCode) {
            PackageManager.PERMISSION_DENIED -> presenter.onRecordAudioPermissionDenied()
            PackageManager.PERMISSION_GRANTED -> presenter.onRecordAudioPermissionGranted()
        }
    }

    override fun setMessageText(text: String) {
        tvFrequencyText.text = text
    }

    override fun setFrequencyGraph(values: List<Pair<Float,Float>>) {
        chartView.apply {
            val dataSet = LineDataSet(
                values.map { Entry(it.first, it.second) },
                "Frequency"
            )
            data = LineData(dataSet)
        }
    }

    override fun requestRecordAudioPermission() {
        requestPermissions(
            arrayOf(Manifest.permission.RECORD_AUDIO),
            RECORD_AUDIO_PERMISSION_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == RECORD_AUDIO_PERMISSION_CODE) {
            when {
                grantResults.getOrNull(0) == PackageManager.PERMISSION_GRANTED -> {
                    presenter.onRecordAudioPermissionGranted()
                }
                shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO) -> {
                    showError("AOAOAO")
                    presenter.onRecordAudioPermissionDenied()
                }
                else -> {
                    presenter.onRecordAudioPermissionDenied()
                }
            }
        }
    }

    companion object {
        private const val RECORD_AUDIO_PERMISSION_CODE = 102
    }

}
