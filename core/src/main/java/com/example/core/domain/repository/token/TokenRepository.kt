package com.example.core.domain.repository.token

interface TokenRepository {
    suspend fun setToken(token : String)
    suspend fun setRefresh(token : String)
    suspend fun getToken(): String?
    suspend fun getRefresh(): String?
    suspend fun clearAuthToken()
}