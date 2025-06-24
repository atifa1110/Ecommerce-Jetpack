package com.example.core.data.data.repository.state

import com.example.core.data.local.datastore.AppStateDataStore
import com.example.core.data.repository.state.StateRepositoryImpl
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

@OptIn(ExperimentalCoroutinesApi::class)
class StateRepositoryImplTest {

    private lateinit var repository: StateRepositoryImpl
    private val appStateDataStore: AppStateDataStore = mockk(relaxed = true)

    @Before
    fun setUp() {
        repository = StateRepositoryImpl(appStateDataStore)
    }

    @Test
    fun `setLogin calls saveLoginState`() = runTest {
        repository.setLogin(true)
        coVerify { appStateDataStore.saveLoginState(true) }
    }

    @Test
    fun `setRegister calls saveRegisterState`() = runTest {
        repository.setRegister(false)
        coVerify { appStateDataStore.saveRegisterState(false) }
    }

    @Test
    fun `setBoarding calls saveOnBoardingState`() = runTest {
        repository.setBoarding(true)
        coVerify { appStateDataStore.saveOnBoardingState(true) }
    }

    @Test
    fun `setProfile calls saveProfileState`() = runTest {
        repository.setProfile(false)
        coVerify { appStateDataStore.saveProfileState(false) }
    }

    @Test
    fun `setDarkMode calls setDarkMode`() = runTest {
        repository.setDarkMode(true)
        coVerify { appStateDataStore.setDarkMode(true) }
    }

    @Test
    fun `setLanguageCode calls setLanguageCode`() = runTest {
        repository.setLanguageCode("id")
        coVerify { appStateDataStore.setLanguageCode("id") }
    }

    @Test
    fun `clearAppState calls clearAppState`() = runTest {
        repository.clearAppState()
        coVerify { appStateDataStore.clearAppState() }
    }

    @Test
    fun `getLogin returns login state flow`() = runTest {
        val expected = true
        every { appStateDataStore.getLoginState() } returns flowOf(expected)

        val result = repository.getLogin().first()
        assertEquals(expected, result)
    }

    @Test
    fun `getRegister returns register state flow`() = runTest {
        val expected = false
        every { appStateDataStore.getRegisterState() } returns flowOf(expected)

        val result = repository.getRegister().first()
        assertEquals(expected, result)
    }

    @Test
    fun `getBoarding returns onboarding state flow`() = runTest {
        every { appStateDataStore.getBoardingState() } returns flowOf(true)

        val result = repository.getBoarding().first()
        assertTrue(result)
    }

    @Test
    fun `getProfile returns profile state flow`() = runTest {
        every { appStateDataStore.getProfileState() } returns flowOf(false)

        val result = repository.getProfile().first()
        assertFalse(result)
    }

    @Test
    fun `getDarkMode returns dark mode flow`() = runTest {
        every { appStateDataStore.getDarkMode() } returns flowOf(true)

        val result = repository.getDarkMode().first()
        assertTrue(result)
    }

    @Test
    fun `getLanguageCode returns language code flow`() = runTest {
        val code = "en"
        every { appStateDataStore.getLanguageCode() } returns flowOf(code)

        val result = repository.getLanguageCode().first()
        assertEquals(code, result)
    }

}