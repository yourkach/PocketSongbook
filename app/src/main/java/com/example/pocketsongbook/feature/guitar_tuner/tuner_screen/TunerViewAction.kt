package com.example.pocketsongbook.feature.guitar_tuner.tuner_screen

sealed class TunerViewAction {
    object OpenPermissionsScreen : TunerViewAction()
    object TunerStateUpdate : TunerViewAction()
}