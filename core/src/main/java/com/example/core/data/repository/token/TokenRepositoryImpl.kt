package com.example.core.data.repository.token

import com.example.core.domain.repository.token.TokenRepository
import com.example.core.data.local.datastore.AuthTokenDataStore
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class TokenRepositoryImpl @Inject constructor(
    private val authTokenDataStore: AuthTokenDataStore
) : TokenRepository {
    override suspend fun setToken(token : String) {
        return authTokenDataStore.saveAccessToken(token)
    }

    override suspend fun setRefresh(token : String) {
        return authTokenDataStore.saveRefreshToken(token)
    }

    override suspend fun getToken(): String? {
        return authTokenDataStore.getAccessToken().firstOrNull()
    }

    override suspend fun getRefresh(): String? {
        return authTokenDataStore.getRefreshToken().firstOrNull()
    }

    override suspend fun clearAuthToken() {
        return authTokenDataStore.clearAuthTokens()
    }
}
