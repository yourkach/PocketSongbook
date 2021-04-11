package com.example.pocketsongbook.common.mvi_core

sealed class LoadableDataState<out TData> {

    class Initial<T> : LoadableDataState<T>() {
        override fun equals(other: Any?): Boolean = other is Initial<*> // important for tests
        override fun hashCode(): Int = javaClass.hashCode()
    }

    class Loading<T> : LoadableDataState<T>() {
        override fun equals(other: Any?): Boolean = other is Loading<*>
        override fun hashCode(): Int = javaClass.hashCode()
    }

    data class Success<out TData>(
        val data: TData
    ) : LoadableDataState<TData>()

    data class Error<T>(
        val error: Throwable? = null
    ) : LoadableDataState<T>()

}

fun <T> T.toSuccessState(): LoadableDataState<T> = LoadableDataState.Success(this)
fun <T> Throwable.toErrorState(): LoadableDataState<T> = LoadableDataState.Error(this)

fun <T> LoadableDataState<T>.getSuccessDataOrNull(): T? {
    return (this as? LoadableDataState.Success<T>)?.data
}