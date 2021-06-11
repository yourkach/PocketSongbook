package com.ybond.core_entities.event_bus

sealed class Event {

    data class FavoritesChange(val url: String, val isAdded: Boolean) : Event()

}