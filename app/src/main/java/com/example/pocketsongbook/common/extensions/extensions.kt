package com.example.pocketsongbook.common.extensions

import kotlinx.coroutines.Job


val Job?.isNullOrCompleted: Boolean
    get() = this?.isActive != true

inline fun Job.alsoInvokeOnCompletion(crossinline handler: (cause: Throwable?) -> Unit): Job {
    return this.also {
        it.invokeOnCompletion { cause -> handler(cause) }
    }
}