package com.example.ecommerceapp.screen.splash

import app.cash.turbine.test
import com.example.core.domain.usecase.GetBoardingStateUseCase
import com.example.core.domain.usecase.GetDarkStateUseCase
import com.example.core.domain.usecase.GetLoginStateUseCase
import com.example.core.domain.usecase.GetProfileStateUseCase
import com.example.core.domain.usecase.GetRegisterStateUseCase
import com.example.core.domain.usecase.SetDarkStateUseCase
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SplashViewModelTest {

    private val getBoardingStateUseCase: GetBoardingStateUseCase = mockk()
    private val getRegisterStateUseCase: GetRegisterStateUseCase = mockk()
    private val getProfileStateUseCase: GetProfileStateUseCase = mockk()
    private val getLoginStateUseCase: GetLoginStateUseCase = mockk()
    private val getDarkStateUseCase: GetDarkStateUseCase = mockk()
    private val setDarkStateUseCase: SetDarkStateUseCase = mockk(relaxed = true)

    private lateinit var viewModel: SplashViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        every { getBoardingStateUseCase() } returns flowOf(true)
        every { getRegisterStateUseCase() } returns flowOf(true)
        every { getProfileStateUseCase() } returns flowOf(true)
        every { getLoginStateUseCase() } returns flowOf(true)
        every { getDarkStateUseCase() } returns flowOf(true)

        viewModel = SplashViewModel(
            getBoardingStateUseCase,
            getRegisterStateUseCase,
            getProfileStateUseCase,
            getLoginStateUseCase,
            getDarkStateUseCase,
            setDarkStateUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onBoardingState emits correct value`() = runTest {
        viewModel.onBoardingState.test {
            assertEquals(true, awaitItem())
            cancel()
        }
    }

    @Test
    fun `onLoginState emits correct value`() = runTest {
        viewModel.onLoginState.test {
            assertEquals(true, awaitItem())
            cancel()
        }
    }

    @Test
    fun `onProfileState emits correct value`() = runTest {
        viewModel.onProfileState.test {
            assertEquals(true, awaitItem())
            cancel()
        }
    }

    @Test
    fun `onRegisterStateUseCase emits correct value`() = runTest {
        viewModel.onRegisterStateUseCase.test {
            assertEquals(true, awaitItem())
            cancel()
        }
    }

    @Test
    fun `isDarkMode emits correct value`() = runTest {
        viewModel.isDarkMode.test {
            assertEquals(true, awaitItem())
            cancel()
        }
    }

    @Test
    fun `toggleDarkMode calls setDarkStateUseCase with correct value`() = runTest {
        viewModel.toggleDarkMode(true)
        advanceUntilIdle()
        coVerify { setDarkStateUseCase(true) }
    }

    @Test
    fun `isLoadingComplete becomes true after delay`() = runTest {
        // Advance time by 100ms to simulate delay
        advanceTimeBy(100)
        // Run any remaining tasks
        advanceUntilIdle()

        assertEquals(true, viewModel.isLoadingComplete.value)
    }

}