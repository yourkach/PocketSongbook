package com.example.pocketsongbook.feature.guitar_tuner

import com.example.pocketsongbook.common.BaseTabContainerFragment
import com.example.pocketsongbook.common.navigation.toScreen
import com.example.pocketsongbook.feature.guitar_tuner.tuner_screen.TunerFragment
import com.github.terrakok.cicerone.Screen

class TunerTabFragment : BaseTabContainerFragment() {
    override val tabName: String = "TUNER"

    override val rootScreen: Screen
        get() = TunerFragment().toScreen()
}