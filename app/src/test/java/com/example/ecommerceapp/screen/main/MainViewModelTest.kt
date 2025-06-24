package com.example.ecommerceapp.screen.main

import com.example.core.domain.usecase.GetCartSizeUseCase
import com.example.core.domain.usecase.GetNotificationSizeUseCase
import com.example.core.domain.usecase.GetUserNameUseCase
import com.example.core.domain.usecase.GetUserProfileUseCase
import com.example.core.domain.usecase.GetWishlistSizeUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    private val getUserProfileUseCase : GetUserProfileUseCase = mockk()
    private val getUserNameUseCase : GetUserNameUseCase= mockk()
    private val getWishlistSizeUseCase : GetWishlistSizeUseCase = mockk()
    private val getCartSizeUseCase : GetCartSizeUseCase = mockk()
    private val getNotificationSizeUseCase : GetNotificationSizeUseCase = mockk()
    private lateinit var viewModel: MainViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadUserData sets userName and userImage`() = runTest {
        every { getUserNameUseCase.invoke() } returns flowOf("John Doe")
        every { getUserProfileUseCase.invoke() } returns flowOf("https://img.url")

        viewModel = MainViewModel(
            getUserProfileUseCase,
            getUserNameUseCase,
            getWishlistSizeUseCase,
            getCartSizeUseCase,
            getNotificationSizeUseCase
        )

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals("John Doe", state.userName)
        assertEquals("https://img.url", state.userImage)

        verify { getUserNameUseCase.invoke() }
        verify { getUserProfileUseCase.invoke() }
    }

    @Test
    fun `loadUserData uses default values when flows return null`() = runTest {
        every { getUserNameUseCase.invoke() } returns flowOf("")
        every { getUserProfileUseCase.invoke() } returns flowOf("")

        viewModel = MainViewModel(
            getUserProfileUseCase,
            getUserNameUseCase,
            getWishlistSizeUseCase,
            getCartSizeUseCase,
            getNotificationSizeUseCase
        )

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals("", state.userName)
        assertEquals("", state.userImage)
    }

    @Test
    fun `getCartSize updates cartSize`() = runTest {
        every { getUserNameUseCase.invoke() } returns flowOf("John Doe")
        every { getUserProfileUseCase.invoke() } returns flowOf("https://img.url")
        coEvery { getCartSizeUseCase.invoke() } returns 4

        viewModel = MainViewModel(
            getUserProfileUseCase,
            getUserNameUseCase,
            getWishlistSizeUseCase,
            getCartSizeUseCase,
            getNotificationSizeUseCase
        )

        viewModel.getCartSize()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(4, state.cartSize)

        coVerify { getCartSizeUseCase.invoke() }
    }

    @Test
    fun `getWishlistSize updates wishlistSize`() = runTest {
        every { getUserNameUseCase.invoke() } returns flowOf("John Doe")
        every { getUserProfileUseCase.invoke() } returns flowOf("https://img.url")
        coEvery { getWishlistSizeUseCase.invoke() } returns 2

        viewModel = MainViewModel(
            getUserProfileUseCase,
            getUserNameUseCase,
            getWishlistSizeUseCase,
            getCartSizeUseCase,
            getNotificationSizeUseCase
        )
        viewModel.getWishlistSize()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(2, state.wishlistSize)
        coVerify { getWishlistSizeUseCase.invoke() }
    }

    @Test
    fun `getNotificationSize updates notificationSize`() = runTest {
        every { getUserNameUseCase.invoke() } returns flowOf("John Doe")
        every { getUserProfileUseCase.invoke() } returns flowOf("https://img.url")
        coEvery { getNotificationSizeUseCase.invoke() } returns 7

        val viewModel = MainViewModel(
            getUserProfileUseCase,
            getUserNameUseCase,
            getWishlistSizeUseCase,
            getCartSizeUseCase,
            getNotificationSizeUseCase
        )

        viewModel.getNotificationSize()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(7, state.notificationSize)

        coVerify { getNotificationSizeUseCase.invoke() }

    }
}
