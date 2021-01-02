package com.example.pocketsongbook.common

import kotlinx.coroutines.*
import moxy.MvpPresenter
import moxy.MvpView
import kotlin.coroutines.CoroutineContext

abstract class BasePresenter<TView : BaseView> : MvpPresenter<TView>(), CoroutineScope {

    private val job = SupervisorJob()

    private val errorHandler = CoroutineExceptionHandler { _, e ->
        e.printStackTrace()
        onFailure(e)
    }

    override val coroutineContext: CoroutineContext =
        Dispatchers.Main + job + errorHandler


    open fun onFailure(e: Throwable) {
        viewState.showError("error: ${e.message}")
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    suspend operator fun <P, R> BaseUseCase<P, R>.invoke(param: P): R {
        return this.execute(param)
    }

    suspend operator fun <R> BaseUseCase<Unit, R>.invoke(): R {
        return this.execute(Unit)
    }

    suspend fun withLoading(block: suspend () -> Unit) {
        viewState.showLoading()
        try {
            block()
        } finally {
            viewState.hideLoading()
        }
    }

}