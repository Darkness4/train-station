package com.example.trainstationapp.core.state

/** This class is used for basic state management (success/failure) */
sealed class State<out T> {
    data class Success<out T>(val value: T) : State<T>()
    data class Failure<out T>(val throwable: Throwable) : State<T>()

    val isSuccess: Boolean
        get() = this is Success
    val isFailure: Boolean
        get() = this is Failure

    fun getOrNull(): T? =
        when (this) {
            is Success -> value
            else -> null
        }

    fun exceptionOrNull(): Throwable? =
        when (this) {
            is Failure -> throwable
            else -> null
        }
}

inline fun <R, T> State<T>.doOnSuccess(
    onSuccess: (value: T) -> R
): R? =
    when (this) {
        is State.Success -> onSuccess(value)
        else -> null
    }

inline fun <R, T> State<T>.doOnFailure(onFailure: (exception: Throwable) -> R): R? =
    when (this) {
        is State.Failure -> onFailure(throwable)
        else -> null
    }

inline fun <R, T> State<T>.fold(
    onSuccess: (value: T) -> R,
    onFailure: (exception: Throwable) -> R
): R =
    when (this) {
        is State.Success -> onSuccess(value)
        is State.Failure -> onFailure(throwable)
    }

inline fun <R, T> State<T>.map(transform: (value: T) -> R): State<R> =
    when (this) {
        is State.Success -> State.Success(transform(value))
        is State.Failure -> State.Failure(throwable)
    }
