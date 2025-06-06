package com.example.ecommerceapp.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AuthTokenDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
        private val EXPIRES_AT_KEY = longPreferencesKey("expires_at")
    }

    suspend fun saveAccessToken(token: String) {
        dataStore.edit { it[ACCESS_TOKEN_KEY] = token }
    }

    fun getAccessToken(): Flow<String> = dataStore.data
        .catch { emit(emptyPreferences()) }
        .map { it[ACCESS_TOKEN_KEY] ?: "" }

    suspend fun saveRefreshToken(token: String) {
        dataStore.edit { it[REFRESH_TOKEN_KEY] = token }
    }

    fun getRefreshToken(): Flow<String> = dataStore.data
        .catch { emit(emptyPreferences()) }
        .map { it[REFRESH_TOKEN_KEY] ?: "" }

    suspend fun saveExpiresAt(timestamp: Long) {
        dataStore.edit { it[EXPIRES_AT_KEY] = timestamp }
    }

    fun getExpiresAt(): Flow<Long> = dataStore.data
        .catch { emit(emptyPreferences()) }
        .map { it[EXPIRES_AT_KEY] ?: 0L }

    suspend fun clearAuthTokens() {
        dataStore.edit { preferences ->
            preferences.remove(ACCESS_TOKEN_KEY)
            preferences.remove(REFRESH_TOKEN_KEY)
            preferences.remove(EXPIRES_AT_KEY)
        }
    }
}
