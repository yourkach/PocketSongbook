package com.example.pocketsongbook.common.mvi_core

fun interface InitialStateProvider<TState> {
    fun getState(): TState
}