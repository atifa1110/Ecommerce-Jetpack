package com.example.core.data.network.utils

import com.example.core.data.network.response.EcommerceResponse
import com.example.core.data.network.response.ErrorResponse
import com.google.gson.Gson
import org.json.JSONObject
import retrofit2.Response

abstract class BaseRemoteDataSource(
    private val gson: Gson
) {
    suspend fun <T> safeApiCall(
        apiCall: suspend () -> Response<T>
    ): EcommerceResponse<T> {
        return try {
            val response = apiCall()
            val body = response.body()

            if (response.isSuccessful && body != null) {
                EcommerceResponse.success(body)
            } else {
                val errorResponse = response.errorBody()?.string()
                val error = errorResponse?.let {
                    try {
                        Gson().fromJson(it, ErrorResponse::class.java)
                    } catch (e: Exception) {
                        ErrorResponse(-1, "Malformed error response")
                    }
                } ?: ErrorResponse(-1, "Unknown error")

                EcommerceResponse.failure(response.code(),error.message)
            }
        } catch (e: Exception) {
            EcommerceResponse.failure(-1, e.localizedMessage ?: "Network error")
        }
    }

    suspend fun <T> safeApiCall2(
        apiCall: suspend () -> Response<T>
    ): EcommerceResponse<T> {
        return try {
            val response = apiCall()
            val body = response.body()

            if (response.isSuccessful && body != null) {
                EcommerceResponse.success(body)
            } else {
                val errorResponse = response.errorBody()?.string()
                val errorMessage = try {
                    JSONObject(errorResponse ?: "").getString("message")
                } catch (e: Exception) {
                    errorResponse ?: "Unknown error"
                }
                EcommerceResponse.failure(response.code(), errorMessage)
            }
        } catch (e: Exception) {
            EcommerceResponse.failure(-1, e.localizedMessage ?: "Network error")
        }
    }

}
