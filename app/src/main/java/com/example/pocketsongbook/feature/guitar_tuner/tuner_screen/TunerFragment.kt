package com.example.pocketsongbook.feature.guitar_tuner.tuner_screen

import android.Manifest
import android.content.res.ColorStateList
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.pocketsongbook.R
import com.example.pocketsongbook.common.BaseFragment
import com.example.pocketsongbook.common.navigation.bottom_navigation.NavigationTab
import com.example.pocketsongbook.common.navigation.bottom_navigation.OnTabSwitchedListener
import com.example.pocketsongbook.common.navigation.toScreen
import com.example.pocketsongbook.databinding.FragmentTunerBinding
import com.example.pocketsongbook.domain.tuner.StringTuningResult
import com.example.pocketsongbook.domain.tuner.string_detection.GuitarString
import com.example.pocketsongbook.feature.guitar_tuner.permissions_screen.MicroPermissionsFragment
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

    private val binding by viewBinding(FragmentTunerBinding::bind)

    private val stringButtonsMap: Map<GuitarString, TextView> by lazy {
        with(binding) {
            mapOf(
                GuitarString.E_1 to btnStringE1,
                GuitarString.B_2 to btnStringB2,
                GuitarString.G_3 to btnStringG3,
                GuitarString.D_4 to btnStringD4,
                GuitarString.A_5 to btnStringA5,
                GuitarString.E_6 to btnStringE6
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnToggleTuner.setOnClickListener { presenter.onToggleTunerClick() }
        binding.btnAutoDetectString.setOnClickListener { presenter.onAutoDetectStringClick() }
        stringButtonsMap.forEach { (string, button) ->
            button.setOnClickListener { presenter.onStringButtonClick(string) }
        }
    }

    override fun updateTunerState(tunerState: TunerState) {
        when (tunerState) {
            is TunerState.Inactive -> updateTuningResult(StringTuningResult.EmptyResult)
            is TunerState.Active -> updateTuningResult(tunerState.tuningResult)
        }
        setToggleTunerButtonState(tunerState.isTunerActive)
    }

    private fun setToggleTunerButtonState(isTunerActive: Boolean) {
        binding.btnToggleTuner.text = when (isTunerActive) {
            true -> getString(R.string.disable_tuner)
            false -> getString(R.string.enable_tuner)
        }
    }

    private val activeStringButtonTintColor by lazy {
        ColorStateList.valueOf(
            ResourcesCompat.getColor(
                resources,
                R.color.colorNavItemSelected,
                null
            )
        )
    }

    private fun updateTuningResult(tuningResult: StringTuningResult) {
        with(binding) {
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
            setAutoDetectButtonVisible(!tuningResult.isAutoDetectModeActive)
        }
    }

    private var currentButtonTargetVisibility: Boolean? = null
    private fun setAutoDetectButtonVisible(targetVisibility: Boolean) {
        binding.btnAutoDetectString.also { btn ->
            if (btn.isVisible == targetVisibility || currentButtonTargetVisibility == targetVisibility) return
            currentButtonTargetVisibility = targetVisibility
            val buttonHeight = resources.getDimension(R.dimen.tuner_text_buttons_height)
            btn.translationY = if (targetVisibility) buttonHeight * 3 else 0.0f
            if (targetVisibility) btn.isVisible = true
            btn.animate().apply {
                translationY(if (targetVisibility) 0.0f else buttonHeight * 3)
                duration = AUTO_DETECT_BTN_ANIMATION_DURATION
                withEndAction {
                    if (!targetVisibility) btn.isVisible = false
                    currentButtonTargetVisibility = null
                }
            }.start()
        }
    }

    override fun onTabSwitched(oldTab: NavigationTab?, newTab: NavigationTab) {
        if (newTab != NavigationTab.Tuner) presenter.onTabSwitchedAway()
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
        private const val AUTO_DETECT_BTN_ANIMATION_DURATION = 150L
    }

}
