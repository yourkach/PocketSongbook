package com.example.pocketsongbook.domain

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

abstract class BaseUseCase<P, R> {

    open val baseDispatcher = Dispatchers.IO

    abstract suspend operator fun invoke(param: P): Result<R>

//    fun execute(param: P): Result<R>{
//
//
//    }

}

// TODO: 08.07.20 сделать extension для выполнения юзкейсов