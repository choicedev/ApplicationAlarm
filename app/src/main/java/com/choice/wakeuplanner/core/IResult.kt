package com.choice.wakeuplanner.core

sealed class IResult<out T> {
    data class Success<T>(val data: T) : IResult<T>()
    data class Error(val errorCode: Int, val message: String) : IResult<Nothing>()
    data class Failed(val exception: Throwable) : IResult<Nothing>()
    data class Loading(val progress: Float? = null) : IResult<Nothing>()

    inline fun <R> map(transform: (T) -> R): IResult<R> = when (this) {
        is Success -> Success(transform(data))
        is Failed -> this
        is Error -> this
        is Loading -> this
    }

    companion object {
        inline fun <T> IResult<T>.watchStatus(
            crossinline onSuccess: (T) -> Unit = {},
            crossinline onError: (Int, String) -> Unit = { _, _ -> },
            crossinline onFailed: (Throwable) -> Unit = {},
            crossinline onLoading: (Float?) -> Unit = {}
        ) {
            when (this) {
                is Success -> onSuccess(this.data)
                is Error -> onError(this.errorCode, this.message)
                is Failed -> onFailed(this.exception)
                is Loading -> onLoading(this.progress)
            }
        }

        fun <T> success (data: T) = Success(data)
        fun failed(exception: Throwable) = Failed(exception)
        fun error(errorCode: Int, message: String) = Error(errorCode, message)
        fun loading (progress: Float? = null) = Loading(progress)
    }
}