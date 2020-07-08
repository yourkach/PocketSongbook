package com.example.pocketsongbook.ui.fragments

import com.example.pocketsongbook.domain.BaseUseCase
import kotlinx.coroutines.*
import moxy.MvpPresenter
import moxy.MvpView
import kotlin.coroutines.CoroutineContext

abstract class BasePresenter<TView : MvpView> : MvpPresenter<TView>(), CoroutineScope {

    private val job = SupervisorJob()

    override val coroutineContext: CoroutineContext =
        Dispatchers.Main + job + CoroutineExceptionHandler { _, e ->
            onFailure(e)
        }


    open fun onFailure(e: Throwable) = Unit

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    fun doInMainContext(action: suspend (Unit) -> Unit) {
        CoroutineScope(coroutineContext).launch {
            action(Unit)
        }
    }

    suspend fun <P, R> BaseUseCase<P, R>.execute(param: P): R {
        return withContext(this.baseDispatcher) {
            this@execute(param)
        }
    }
}