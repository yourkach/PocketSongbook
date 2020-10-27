package com.example.pocketsongbook.common

import moxy.MvpView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.SkipStrategy
import moxy.viewstate.strategy.StateStrategyType

interface BaseView : MvpView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showMessage(messageId: Int)

    @StateStrategyType(SkipStrategy::class)
    fun showLoading()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun hideLoading()

}