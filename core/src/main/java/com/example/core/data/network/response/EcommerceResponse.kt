package com.example.core.data.network.response

sealed class EcommerceResponse<out T> {
    data class Success<T>(val value: T) : EcommerceResponse<T>()
    data class Failure(val code: Int, val error: String) : EcommerceResponse<Nothing>()
    object Loading : EcommerceResponse<Nothing>()

    companion object {
        fun <T> success(data: T): Success<T> = Success(data)
        fun failure(code: Int, error: String): Failure = Failure(code, error)
    }
}

