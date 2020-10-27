package com.example.pocketsongbook.common

import kotlinx.coroutines.*
import moxy.MvpPresenter
import moxy.MvpView
import kotlin.coroutines.CoroutineContext

abstract class BasePresenter<TView : BaseView> : MvpPresenter<TView>(), CoroutineScope {

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

    suspend operator fun <P,R> BaseUseCase<P,R>.invoke(param : P) : R {
        return this.execute(param)
    }

    suspend operator fun <R> BaseUseCase<Unit,R>.invoke() : R {
        return this.execute(Unit)
    }
}