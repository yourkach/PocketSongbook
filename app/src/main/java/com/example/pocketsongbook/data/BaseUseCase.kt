package com.example.pocketsongbook.data

import kotlinx.coroutines.Dispatchers

abstract class BaseUseCase<P, R> {

    abstract suspend operator fun invoke(param: P): R

//    abstract suspend operator fun invoke(param: P): Result<R>

//    fun execute(param: P): Result<R>{
//
//
//    }

}

// TODO: 08.07.20 сделать extension для выполнения юзкейсов