package com.example.trainstationapp.core.result

sealed class Result<out T> {
    data class Success<out T>(val value: T) : Result<T>()
    data class Failure<out T>(val throwable: Throwable) : Result<T>()
    val isSuccess: Boolean
        get() = this is Success
    val isFailure: Boolean
        get() = this is Failure

    fun valueOrNull(): T? {
        return if (this is Success) value
        else null
    }

    fun exceptionOrNull(): Throwable? {
        return if (this is Failure) throwable
        else null
    }

    fun <R> doOnSuccess(
        onSuccess: (value: T) -> R,
    ): R? {
        return if (this is Success) onSuccess(value)
        else null
    }

    fun <R> doOnFailure(
        onFailure: (exception: Throwable) -> R
    ): R? {
        return if (this is Failure) onFailure(throwable)
        else null
    }

    fun <R> fold(
        onSuccess: (value: T) -> R,
        onFailure: (exception: Throwable) -> R
    ): R {
        return when (this) {
            is Success -> onSuccess(value)
            is Failure -> onFailure(throwable)
        }
    }

    fun <R> map(transform: (value: T) -> R): Result<R> {
        return when (this) {
            is Success -> Success(transform(value))
            is Failure -> Failure(throwable)
        }
    }
}
