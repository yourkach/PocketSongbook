package com.example.pocketsongbook.common

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel : ViewModel(), CoroutineScope {

    private val job = SupervisorJob()

    private val errorHandler = CoroutineExceptionHandler { _, e ->
        e.printStackTrace()
        onFailure(e)
    }

    override val coroutineContext: CoroutineContext =
        Dispatchers.Main + job + errorHandler

    open fun onFailure(e: Throwable) {}
}