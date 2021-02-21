package com.example.pocketsongbook.domain.event_bus

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventBus @Inject constructor() {

    private val _eventsFlow = MutableSharedFlow<Event>()

    val eventsFlow: SharedFlow<Event> = _eventsFlow

    suspend fun postEvent(event: Event) {
        Timber.d("Posted event: $event")
        _eventsFlow.emit(event)
    }

}

