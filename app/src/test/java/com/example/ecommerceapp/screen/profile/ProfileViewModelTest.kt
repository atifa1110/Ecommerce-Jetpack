package com.example.ecommerceapp.screen.profile

import android.net.Uri
import app.cash.turbine.test
import com.example.core.data.network.response.EcommerceResponse
import com.example.core.domain.usecase.ProfileUseCase
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModelTest {

    private val profileUseCase: ProfileUseCase = mockk()
    private lateinit var viewModel: ProfileViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        viewModel = ProfileViewModel(profileUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `setImageUri updates imageUri`() {
        val fakeUri = mockk<Uri>()
        viewModel.setImageUri(fakeUri)
        assertEquals(fakeUri, viewModel.uiState.value.imageUri)
    }

    @Test
    fun `setUserImage updates userImage`() {
        val file = File("dummy.jpg")
        viewModel.setUserImage(file)
        assertEquals(file, viewModel.uiState.value.userImage)
    }

    @Test
    fun `setUserName updates userName`() {
        viewModel.setUserName("Alice")
        assertEquals("Alice", viewModel.uiState.value.userName)
    }

    @Test
    fun `showDialog updates true showImageDialog`() {
        viewModel.showDialog(true)
        assertTrue(viewModel.uiState.value.showImageDialog)
    }

    @Test
    fun `showDialog updates false showImageDialog`() {
        viewModel.showDialog(false)
        assertFalse(viewModel.uiState.value.showImageDialog)
    }

    @Test
    fun `onSetProfileClick emits error when file or name is missing`() = runTest {
        viewModel.setUserName("")
        viewModel.setUserImage(null)

        val job = launch {
            viewModel.eventFlow.test {
                val events = awaitItem()
                assertTrue(events is ProfileEvent.ShowSnackbar)
                assertEquals(
                    "Please input file and username correctly",
                    (events as ProfileEvent.ShowSnackbar).message
                )
            }
        }
        viewModel.onSetProfileClick()
        advanceUntilIdle()
        job.join()
    }


    @Test
    fun `onSetProfileClick handles success flow`() = runTest {
        val file = File("temp.jpg")
        val username = "Alice"
        viewModel.setUserImage(file)
        viewModel.setUserName(username)

        val flow = flowOf(
            EcommerceResponse.Loading,
            EcommerceResponse.Success("Upload is Success")
        )
        coEvery {
            profileUseCase.invoke(any(), any())
        } returns flow

        val job = launch {
            viewModel.eventFlow.test {
                val events = awaitItem()
                assertTrue(events is ProfileEvent.ShowSnackbar)
                assertEquals("Upload is Success", (events as ProfileEvent.ShowSnackbar).message)
            }
            cancel()
        }

        viewModel.onSetProfileClick()
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertTrue(state.isSuccess)
            assertFalse(state.isError)
        }

        job.join()
    }



}
