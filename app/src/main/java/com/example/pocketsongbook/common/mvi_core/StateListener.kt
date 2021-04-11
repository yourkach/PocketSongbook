package com.example.pocketsongbook.common.mvi_core

fun interface StateListener<TState> {
    fun onNewState(state: TState)
}