package com.example.pocketsongbook.feature.guitar_tuner.tuner_screen

import android.Manifest
import android.content.res.ColorStateList
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.example.pocketsongbook.R
import com.example.pocketsongbook.common.BaseFragment
import com.example.pocketsongbook.common.navigation.bottom_navigation.NavigationTab
import com.example.pocketsongbook.common.navigation.bottom_navigation.OnTabSwitchedListener
import com.example.pocketsongbook.common.navigation.toScreen
import com.example.pocketsongbook.domain.tuner.MutableStringTuningResult
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

    private val stringButtonsMap: Map<GuitarString, TextView> by lazy {
        mapOf(
            GuitarString.E_1 to btnStringE1,
            GuitarString.B_2 to btnStringB2,
            GuitarString.G_3 to btnStringG3,
            GuitarString.D_4 to btnStringD4,
            GuitarString.A_5 to btnStringA5,
            GuitarString.E_6 to btnStringE6
        )
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
            getString(R.string.disable_tuner)
        } else {
            presenter.onStopClick()
            getString(R.string.enable_tuner)
        }
    }

    private val activeStringButtonTintColor by lazy {
        ColorStateList.valueOf(ResourcesCompat.getColor(resources, R.color.colorNavItemSelected, null))
    }
    override fun updateTunerResult(tuningResult: StringTuningResult) {
        val offset = tuningResult.percentOffset
        val (activePb, inactivePb) = when {
            offset > 0 -> pbOffsetUp to pbOffsetDown
            else -> pbOffsetDown to pbOffsetUp
        }
        activePb.progress = if (tuningResult.string != GuitarString.UNDEFINED) {
            (PROGRESS_MAX * offset.absoluteValue).roundToInt().coerceAtMost(PROGRESS_MAX)
        } else 0
        inactivePb.progress = 0
        stringButtonsMap.forEach { (string, button) ->
            button.backgroundTintList = when (string) {
                tuningResult.string -> activeStringButtonTintColor
                else -> null
            }
        }
    }

    override fun onTabSwitched(oldTab: NavigationTab?, newTab: NavigationTab) {
        // TODO: 09.05.2021 вынести логику в презентер
        if (newTab != NavigationTab.Tuner && isTunerActive) {
            toggleTuner()
            updateTunerResult(MutableStringTuningResult(GuitarString.UNDEFINED, 0.0, 0.0))
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

    companion object {
        private const val PROGRESS_MAX = 1000
    }

}
