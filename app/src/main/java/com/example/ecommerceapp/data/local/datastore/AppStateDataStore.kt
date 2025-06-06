package com.example.ecommerceapp.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AppStateDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val ONBOARDING_STATE_KEY = booleanPreferencesKey("onboarding")
        private val LOGIN_STATE_KEY = booleanPreferencesKey("login")
        private val REGISTER_STATE_KEY = booleanPreferencesKey("register")
        private val PROFILE_STATE_KEY = booleanPreferencesKey("profile")
        private val DARK_MODE_STATE_KEY = booleanPreferencesKey("darkMode")
    }

    suspend fun saveOnBoardingState(completed: Boolean) {
        dataStore.edit { it[ONBOARDING_STATE_KEY] = completed }
    }

    fun getBoardingState(): Flow<Boolean> = dataStore.data
        .catch { emit(emptyPreferences()) }
        .map { it[ONBOARDING_STATE_KEY] ?: false }

    suspend fun saveLoginState(completed: Boolean) {
        dataStore.edit { it[LOGIN_STATE_KEY] = completed }
    }

    fun getLoginState(): Flow<Boolean> = dataStore.data
        .catch { emit(emptyPreferences()) }
        .map { it[LOGIN_STATE_KEY] ?: false }

    suspend fun saveRegisterState(completed: Boolean) {
        dataStore.edit { it[REGISTER_STATE_KEY] = completed }
    }

    fun getRegisterState(): Flow<Boolean> = dataStore.data
        .catch { emit(emptyPreferences()) }
        .map { it[REGISTER_STATE_KEY] ?: false }

    suspend fun saveProfileState(completed: Boolean) {
        dataStore.edit { it[PROFILE_STATE_KEY] = completed }
    }

    fun getProfileState(): Flow<Boolean> = dataStore.data
        .catch { emit(emptyPreferences()) }
        .map { it[PROFILE_STATE_KEY] ?: false }

    suspend fun setDarkMode(isDarkMode: Boolean) {
        dataStore.edit { it[DARK_MODE_STATE_KEY] = isDarkMode }
    }

    fun getDarkMode(): Flow<Boolean> = dataStore.data
        .catch { emit(emptyPreferences()) }
        .map { it[DARK_MODE_STATE_KEY] ?: false }

    suspend fun clearAppState() {
        dataStore.edit { preferences ->
            preferences.remove(LOGIN_STATE_KEY)
            preferences.remove(REGISTER_STATE_KEY)
            preferences.remove(PROFILE_STATE_KEY)
        }
    }
}