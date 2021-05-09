package com.example.pocketsongbook.feature.guitar_tuner.tuner_screen

import android.Manifest
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import com.example.pocketsongbook.R
import com.example.pocketsongbook.common.BaseFragment
import com.example.pocketsongbook.common.navigation.bottom_navigation.NavigationTab
import com.example.pocketsongbook.common.navigation.bottom_navigation.OnTabSwitchedListener
import com.example.pocketsongbook.common.navigation.toScreen
import com.example.pocketsongbook.domain.tuner.StringTuningResult
import com.example.pocketsongbook.domain.tuner.string_detection.GuitarString
import com.example.pocketsongbook.feature.guitar_tuner.permissions_screen.MicroPermissionsFragment
import kotlinx.android.synthetic.main.fragment_tuner.*
import moxy.ktx.moxyPresenter
import javax.inject.Inject
import javax.inject.Provider
import kotlin.math.absoluteValue
import kotlin.math.roundToInt


class TunerFragment : BaseFragment(R.layout.fragment_tuner), TunerView, OnTabSwitchedListener {

    @Inject
    lateinit var presenterProvider: Provider<TunerPresenter>

    private val presenter by moxyPresenter {
        presenterProvider.get()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnToggleTuner.setOnClickListener {
            toggleTuner()
        }
    }

    private var isTunerActive = false
    private fun toggleTuner() {
        isTunerActive = !isTunerActive
        btnToggleTuner.text = if (isTunerActive) {
            presenter.onStartClick()
            "toggle (on)"
        } else {
            presenter.onStopClick()
            "toggle (off)"
        }
    }

    override fun updateTunerResult(result: StringTuningResult) {
        val offset = result.percentOffset
        val (activePb, inactivePb) = when {
            offset > 0 -> pbOffsetUp to pbOffsetDown
            else -> pbOffsetDown to pbOffsetUp
        }
        activePb.progress = if (result.string != GuitarString.UNDEFINED) {
            (PROGRESS_MAX * offset.absoluteValue).roundToInt().coerceAtMost(PROGRESS_MAX)
        } else 0
        inactivePb.progress = 0
        tvSomeText.text = when (result.string) {
            GuitarString.E_1 -> "E-1"
            GuitarString.B_2 -> "B-2"
            GuitarString.G_3 -> "G-3"
            GuitarString.D_4 -> "D-4"
            GuitarString.A_5 -> "A-5"
            GuitarString.E_6 -> "E-6"
            GuitarString.UNDEFINED -> ""
        }
    }

    override fun onTabSwitched(oldTab: NavigationTab?, newTab: NavigationTab) {
        if (newTab != NavigationTab.Tuner && isTunerActive) {
            toggleTuner()
        }
    }

    override fun onResume() {
        super.onResume()
        checkMicroPermission()
    }

    private fun checkMicroPermission() {
        if (!isPermissionGranted(Manifest.permission.RECORD_AUDIO)) {
            presenter.onRecordAudioPermissionDenied()
        }
    }

    override fun toMicroPermissionScreen() {
        Handler(Looper.getMainLooper()).post {
            router.replaceScreen(MicroPermissionsFragment().toScreen())
        }
    }

    override fun setMessageText(text: String) {
        tvSomeText.text = text
    }

    companion object {
        private const val PROGRESS_MAX = 1000
    }

}
