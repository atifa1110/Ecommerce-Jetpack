package com.example.ecommerceapp.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val USERNAME_KEY = stringPreferencesKey("username")
        private val USER_IMAGE_KEY = stringPreferencesKey("user_image")
    }

    suspend fun saveUserName(userName: String) {
        dataStore.edit { it[USERNAME_KEY] = userName }
    }

    fun getUserName(): Flow<String> = dataStore.data
        .catch { emit(emptyPreferences()) }
        .map { it[USERNAME_KEY] ?: "" }

    suspend fun saveUserImage(userImageUrl: String) {
        dataStore.edit { it[USER_IMAGE_KEY] = userImageUrl }
    }

    fun getUserImage(): Flow<String> = dataStore.data
        .catch { emit(emptyPreferences()) }
        .map { it[USER_IMAGE_KEY] ?: "" }

    suspend fun clearUserData() {
        dataStore.edit { preferences ->
            preferences.remove(USERNAME_KEY)
            preferences.remove(USER_IMAGE_KEY)
        }
    }
}
