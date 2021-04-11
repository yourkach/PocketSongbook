package com.example.pocketsongbook.common.mvi_core

import timber.log.Timber
import java.lang.ref.WeakReference

abstract class BaseMviReducer<TState, TEvent>(initialState: TState) : MviReducer<TState, TEvent> {

    protected val tag: String = this::class.java.simpleName

    private var stateListenerReference: WeakReference<StateListener<TState>>? = null

    override var currentState: TState = initialState
        set(newValue) {
            field.also { lastValue ->
                field = newValue
                if (lastValue != newValue) {
                    stateListenerReference?.get()?.onNewState(newValue)
                }
            }
        }

    fun onNewState(listener: StateListener<TState>) {
        stateListenerReference = WeakReference(listener)
    }

    override fun obtainEvent(event: TEvent) {
        Timber.tag(tag).d("obtain event: $event")
        currentState = reduceEvent(currentState, event)
    }

    override fun subscribe(listener: StateListener<TState>, callOnSubscribe: Boolean) {
        stateListenerReference = WeakReference(listener)
        listener.onNewState(currentState)
    }

    override fun unsubscribe(listener: StateListener<TState>) {
        if (stateListenerReference?.get() == listener) {
            stateListenerReference = null
        }
    }

    protected abstract fun reduceEvent(
        currentState: TState,
        event: TEvent
    ): TState

}
