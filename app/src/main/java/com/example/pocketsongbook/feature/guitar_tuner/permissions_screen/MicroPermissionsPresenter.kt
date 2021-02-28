package com.example.pocketsongbook.feature.guitar_tuner.permissions_screen

import com.example.pocketsongbook.common.BasePresenter
import com.example.pocketsongbook.common.BaseView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

interface MicroPermissionsView : BaseView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun requestPermission()

}


class MicroPermissionsPresenter : BasePresenter<MicroPermissionsView>() {



}
