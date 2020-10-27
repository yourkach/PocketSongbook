package com.example.pocketsongbook.common

abstract class BaseUseCase<P, R> {

    abstract suspend fun execute(param: P): R

}



// TODO: 08.07.20 сделать extension для выполнения юзкейсов