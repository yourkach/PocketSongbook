package com.example.pocketsongbook.common.extensions

import kotlinx.coroutines.Job
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun setAndCancelJob() = object : ReadWriteProperty<Any?, Job?> {
    private var job: Job? = null

    override fun getValue(thisRef: Any?, property: KProperty<*>): Job? {
        return job
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Job?) {
        job?.cancel()
        job = value
    }
}