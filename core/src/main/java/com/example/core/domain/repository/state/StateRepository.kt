package com.example.core.domain.repository.state

import kotlinx.coroutines.flow.Flow

interface StateRepository {
    suspend fun setLogin(complete: Boolean)
    suspend fun setRegister(complete: Boolean)
    suspend fun setBoarding(complete: Boolean)
    suspend fun setProfile(complete: Boolean)
    suspend fun setDarkMode(isDarkMode: Boolean)
    suspend fun setLanguageCode(languageCode: String)
    fun getLogin(): Flow<Boolean>
    fun getRegister(): Flow<Boolean>
    fun getBoarding(): Flow<Boolean>
    fun getProfile(): Flow<Boolean>
    fun getDarkMode(): Flow<Boolean>
    fun getLanguageCode(): Flow<String>
    suspend fun clearAppState()
}