package com.example.ecommerceapp.data.network.response

sealed class PagingError(message: String? = null) : Throwable(message) {
    data class ApiError(val code: Int, val errorMessage: String) : PagingError(errorMessage)
    object NotFoundError : PagingError("Not found")
    object ConnectionError : PagingError("Connection error")
}

