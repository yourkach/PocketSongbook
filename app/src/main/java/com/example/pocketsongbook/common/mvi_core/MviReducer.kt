package com.example.pocketsongbook.common.mvi_core

import com.example.pocketsongbook.common.Emitter

interface MviReducer<TState, TEvent> : Emitter<StateListener<TState>> {
    val currentState: TState
    fun obtainEvent(event: TEvent)
}

