package com.ybond.domain.usecases.event_bus

import com.ybond.core_entities.event_bus.Event
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventBus @Inject constructor() {

    private val _eventsFlow = MutableSharedFlow<Event>()

    val eventsFlow: SharedFlow<Event> = _eventsFlow

    suspend fun postEvent(event: Event) {
        _eventsFlow.emit(event)
    }

}
