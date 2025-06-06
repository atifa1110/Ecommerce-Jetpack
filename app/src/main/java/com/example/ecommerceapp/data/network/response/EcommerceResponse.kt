package com.example.ecommerceapp.data.network.response

//sealed class EcommerceResponse<out T> {
//    data class Success<out T>(val value: T) : EcommerceResponse<T>()
//    data class Failure<out T>(val error: String, val data: T? = null) : EcommerceResponse<T>()
//    object Loading : EcommerceResponse<Nothing>()
//
//    companion object {
//        fun <T> success(data: T): EcommerceResponse<T> = Success(data)
//        fun <T> failure(error: String, data: T? = null): EcommerceResponse<T> = Failure(error, data)
//        fun loading(): EcommerceResponse<Nothing> = Loading
//    }
//}

sealed class EcommerceResponse<out T> {
    data class Success<T>(val value: T) : EcommerceResponse<T>()
    data class Failure(val code: Int, val error: String) : EcommerceResponse<Nothing>()
    object Loading : EcommerceResponse<Nothing>()

    companion object {
        fun <T> success(data: T): Success<T> = Success(data)
        fun failure(code: Int, error: String): Failure = Failure(code, error)
    }
}

