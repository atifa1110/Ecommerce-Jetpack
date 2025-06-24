package com.example.core.data.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.emptyPreferences
import com.example.core.data.local.datastore.AppStateDataStore
import com.example.core.data.local.datastore.AuthTokenDataStore
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.File

class AuthTokenDataStoreTest {

    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var authTokenDataStore: AuthTokenDataStore
    private lateinit var prefsFlow: MutableStateFlow<Preferences>

    @Before
    fun setup() {
        dataStore = mockk(relaxed = true)
        prefsFlow = MutableStateFlow(emptyPreferences())

        coEvery { dataStore.updateData(any()) } coAnswers {
            val block = it.invocation.args[0] as suspend (Preferences) -> Preferences
            val updated = block(prefsFlow.value)
            prefsFlow.value = updated
            updated
        }

        every { dataStore.data } returns prefsFlow

        authTokenDataStore = AuthTokenDataStore(dataStore)
    }

    @Test
    fun `save and read access token`() = runBlocking {
        authTokenDataStore.saveAccessToken("access_123")
        val result = authTokenDataStore.getAccessToken().first()
        assertEquals("access_123", result)
    }

    @Test
    fun `save and read refresh token`() = runBlocking {
        authTokenDataStore.saveRefreshToken("refresh_456")
        val result = authTokenDataStore.getRefreshToken().first()
        assertEquals("refresh_456", result)
    }

    @Test
    fun `save and read expires at`() = runBlocking {
        authTokenDataStore.saveExpiresAt(1234567890L)
        val result = authTokenDataStore.getExpiresAt().first()
        assertEquals(1234567890L, result)
    }

    @Test
    fun `clear all tokens`() = runBlocking {
        // Save values
        authTokenDataStore.saveAccessToken("access")
        authTokenDataStore.saveRefreshToken("refresh")
        authTokenDataStore.saveExpiresAt(9999L)

        // Clear all
        authTokenDataStore.clearAuthTokens()

        // Assert cleared
        assertEquals("", authTokenDataStore.getAccessToken().first())
        assertEquals("", authTokenDataStore.getRefreshToken().first())
        assertEquals(0L, authTokenDataStore.getExpiresAt().first())
    }
}
