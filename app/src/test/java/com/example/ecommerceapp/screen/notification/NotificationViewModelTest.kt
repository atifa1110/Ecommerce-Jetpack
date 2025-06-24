package com.example.ecommerceapp.screen.notification

import app.cash.turbine.test
import com.example.core.domain.model.NotificationModel
import com.example.core.data.network.response.EcommerceResponse
import com.example.core.ui.mapper.asNotification
import com.example.core.domain.usecase.GetNotificationUseCase
import com.example.core.domain.usecase.UpdateNotificationUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
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
class NotificationViewModelTest {

    private lateinit var getNotificationUseCase: GetNotificationUseCase
    private lateinit var updateNotificationUseCase: UpdateNotificationUseCase
    private lateinit var viewModel: NotificationViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        getNotificationUseCase = mockk()
        updateNotificationUseCase = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getNotification - success updates uiState`() = runTest {
        // Given
        val fakeResponse = listOf(
            NotificationModel(
                id = 1,
                title = "New",
                body = "body",
                image = "image",
                type = "promo",
                date = "date",
                time = "time",
                isRead = false
            )
        )

        val flow = flowOf(EcommerceResponse.Success(fakeResponse))
        coEvery { getNotificationUseCase.invoke() } returns flow

        // 🟢 Buat viewModel setelah mocking selesai
        viewModel = NotificationViewModel(getNotificationUseCase, updateNotificationUseCase)

        // Tunggu semua coroutine selesai
        advanceUntilIdle()

        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(false, state.isError)
            assertEquals(false, state.isLoading)
            assertEquals(fakeResponse.asNotification(), state.notifications)
        }
    }

    @Test
    fun `getNotification - failure sets isError true`() = runTest {
        val flow = flowOf(EcommerceResponse.Failure(400,"error"))
        coEvery { getNotificationUseCase.invoke() } returns flow

        viewModel = NotificationViewModel(getNotificationUseCase, updateNotificationUseCase)

        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state.isError)
        }
    }

    @Test
    fun `getNotification then updateNotification - updates isRead on existing notification`() = runTest {
        // Step 1: Fake notification awal (belum dibaca)
        val fakeNotificationModel = listOf(NotificationModel(
            id = 1, title = "New", body = "body", image = "image",
            type = "promo", date = "date", time = "time", isRead = false
        ))

        // Step 2: Mock use case
        coEvery { getNotificationUseCase.invoke() } returns flowOf(EcommerceResponse.Success(fakeNotificationModel))
        coEvery { updateNotificationUseCase.invoke(1, true) } returns Unit

        // Step 3: Inisialisasi ViewModel
        viewModel = NotificationViewModel(getNotificationUseCase, updateNotificationUseCase)

        // Step 4: Tunggu fetch selesai
        advanceUntilIdle()

        // Step 5: Update notification ke read = true
        viewModel.updateNotification(1, true)
        advanceUntilIdle()

        // Step 6: Assert hasil akhir
        viewModel.uiState.test {
            val updated = awaitItem()
            println("Final UI State: ${updated.notifications}")
            val result = updated.notifications
            assertTrue(result[0].isRead)
        }

        // Step 7: Verifikasi bahwa use case update dipanggil
        coVerify { updateNotificationUseCase.invoke(1, true) }
    }

}