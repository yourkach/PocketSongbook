package com.example.pocketsongbook.common.mvi_core

interface EventReducer<TEvent, TState> {
    fun reduceBy(
        currentState: TState,
        event: TEvent
    ): TState
}