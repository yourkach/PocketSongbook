package com.example.pocketsongbook.domain.event_bus

import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class SubscribeToEventsUseCase @Inject constructor(
    val eventBus: EventBus
) {

    suspend inline operator fun invoke(crossinline onEvent: suspend (event: Event) -> Unit) {
        eventBus.eventsFlow.collect {
            onEvent(it)
        }
    }

}
