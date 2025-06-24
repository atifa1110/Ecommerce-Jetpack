package com.example.ecommerceapp.screen.home

import app.cash.turbine.test
import com.example.core.domain.usecase.ClearAppStateUseCase
import com.example.core.domain.usecase.ClearAuthTokenUseCase
import com.example.core.domain.usecase.ClearUserDataUseCase
import com.example.core.domain.usecase.GetLanguageCodeStateUseCase
import com.example.core.domain.usecase.SetLanguageCodeUseCase
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
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
class HomeViewModelTest {

    private val clearAuthTokenUseCase : ClearAuthTokenUseCase = mockk(relaxed = true)
    private val clearAppStateUseCase : ClearAppStateUseCase = mockk(relaxed = true)
    private val clearUserDataUseCase : ClearUserDataUseCase = mockk(relaxed = true)
    private val getLanguageCodeStateUseCase : GetLanguageCodeStateUseCase= mockk()
    private val setLanguageCodeUseCase : SetLanguageCodeUseCase = mockk(relaxed = true)

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onLogoutClick invokes clear use cases in order`() = runTest {
        every { getLanguageCodeStateUseCase.invoke() } returns flowOf("en")
        coEvery{ clearAppStateUseCase.invoke() } just Runs
        coEvery { clearUserDataUseCase.invoke() } just Runs
        coEvery { clearAuthTokenUseCase.invoke() } just Runs

        viewModel = HomeViewModel(
            clearAuthTokenUseCase,
            clearAppStateUseCase,
            clearUserDataUseCase,
            getLanguageCodeStateUseCase,
            setLanguageCodeUseCase
        )

        viewModel.onLogoutClick()
        advanceUntilIdle()

        verify { getLanguageCodeStateUseCase.invoke() }
        coVerify { clearAppStateUseCase.invoke() }
        coVerify { clearUserDataUseCase.invoke() }
        coVerify { clearAuthTokenUseCase.invoke() }
    }

    @Test
    fun `languageCode emits correct default value`() = runTest {
        every { getLanguageCodeStateUseCase.invoke() } returns flowOf("en")

        viewModel = HomeViewModel(
            clearAuthTokenUseCase,
            clearAppStateUseCase,
            clearUserDataUseCase,
            getLanguageCodeStateUseCase,
            setLanguageCodeUseCase
        )
        advanceUntilIdle()

        viewModel.languageCode.test {
            assertEquals("en", awaitItem())
            cancel()
        }

        verify { getLanguageCodeStateUseCase.invoke() }
    }

    @Test
    fun `setLanguage invokes use case and updates languageCode to id`() = runTest {
        val languageFlow = MutableStateFlow("en") // initial value

        coEvery { getLanguageCodeStateUseCase.invoke() } returns languageFlow
        coEvery { setLanguageCodeUseCase.invoke("id") } answers {
            languageFlow.value = "id" // simulate DataStore or repository update
        }

        viewModel = HomeViewModel(
            clearAuthTokenUseCase,
            clearAppStateUseCase,
            clearUserDataUseCase,
            getLanguageCodeStateUseCase = getLanguageCodeStateUseCase,
            setLanguageCodeUseCase = setLanguageCodeUseCase
        )

        viewModel.setLanguage("id")
        advanceTimeBy(50)

        viewModel.languageCode.test {
            assertEquals("id", awaitItem()) // ✅ updated value
            cancel()
        }

        coVerify { setLanguageCodeUseCase.invoke("id") }
    }

    @Test
    fun `setLanguage invokes use case en with correct code`() = runTest {
        coEvery { setLanguageCodeUseCase.invoke("en") } just Runs
        coEvery{ getLanguageCodeStateUseCase.invoke() } returns flowOf("en")

        viewModel = HomeViewModel(
            clearAuthTokenUseCase,
            clearAppStateUseCase,
            clearUserDataUseCase,
            getLanguageCodeStateUseCase,
            setLanguageCodeUseCase
        )

        viewModel.setLanguage("en")
        advanceUntilIdle()

        viewModel.languageCode.test {
            assertEquals("en",awaitItem())
        }

        coVerify { setLanguageCodeUseCase.invoke("en") }
        verify { getLanguageCodeStateUseCase.invoke() }
    }

}