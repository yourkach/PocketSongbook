package com.example.pocketsongbook.common

interface Emitter<TListener> {
    fun subscribe(listener: TListener, callOnSubscribe: Boolean = true)
    fun unsubscribe(listener: TListener)
}