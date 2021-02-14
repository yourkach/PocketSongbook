package com.example.pocketsongbook.domain.event_bus

import android.util.Log
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collect
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventBus @Inject constructor() {

    private val _eventsFlow = MutableSharedFlow<Event>()

    val eventsFlow: SharedFlow<Event> = _eventsFlow

    suspend fun postEvent(event: Event) {
        Log.d(this::class.simpleName,"Posted event: $event")
        _eventsFlow.emit(event)
    }

}

