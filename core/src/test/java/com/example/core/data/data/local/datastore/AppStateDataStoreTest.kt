package com.example.core.data.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import app.cash.turbine.test
import com.example.core.data.local.datastore.AppStateDataStore
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertFalse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AppStateDataStoreTest {
    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var appStateDataStore: AppStateDataStore
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

        appStateDataStore = AppStateDataStore(dataStore)
    }

    @Test
    fun `test save and get onboarding state`() = runTest {
        appStateDataStore.saveOnBoardingState(true)
        val result = appStateDataStore.getBoardingState().first()
        assertTrue(result)
    }

    @Test
    fun `test save and get login state`() = runTest {
        appStateDataStore.saveLoginState(true)
        val result = appStateDataStore.getLoginState().first()
        assertTrue(result)
    }

    @Test
    fun `test save and get register state`() = runTest {
        appStateDataStore.saveRegisterState(true)
        val result = appStateDataStore.getRegisterState().first()
        assertTrue(result)
    }

    @Test
    fun `test save and get profile state`() = runTest {
        appStateDataStore.saveProfileState(true)
        val result = appStateDataStore.getProfileState().first()
        assertTrue(result)
    }

    @Test
    fun `test save and get dark mode`() = runTest{
        appStateDataStore.setDarkMode(true)
        val result = appStateDataStore.getDarkMode().first()
        assertTrue(result)
    }

    @Test
    fun `test save and get language code`() = runTest {
        appStateDataStore.setLanguageCode("id")
        val result = appStateDataStore.getLanguageCode().first()
        assertEquals("id",result)
    }

    @Test
    fun `test clear app state`() = runTest {
        appStateDataStore.saveLoginState(true)

        appStateDataStore.getLoginState().test {
            val state = awaitItem()
            assertTrue(state)
        }
        appStateDataStore.clearAppState()

        appStateDataStore.getLoginState().test {
            val state = awaitItem()
            assertFalse(state)
        }
    }
}
