package com.example.ecommerceapp.screen.login

import app.cash.turbine.test
import com.example.core.domain.model.UserModel
import com.example.core.data.network.response.EcommerceResponse
import com.example.ecommerceapp.firebase.AuthAnalytics
import com.example.core.domain.usecase.LoginUseCase
import com.example.core.domain.usecase.UpdateFcmTokenUseCase
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private lateinit var viewModel: LoginViewModel
    private val loginUseCase: LoginUseCase = mockk()
    private val updateFcmTokenUseCase : UpdateFcmTokenUseCase = mockk()
    private val authAnalytics: AuthAnalytics = mockk(relaxed = true) // relaxed to avoid verifying all

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        viewModel = LoginViewModel(loginUseCase,updateFcmTokenUseCase,authAnalytics)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onEmailChange with invalid email sets error`() {
        viewModel.onEmailChange("invalid-email")
        val state = viewModel.uiState.value
        assertEquals("Invalid email format", state.emailError)
    }

    @Test
    fun `onPasswordChange with short password sets error`() {
        viewModel.onPasswordChange("123")
        val state = viewModel.uiState.value
        assertEquals("Password must be at least 8 characters", state.passwordError)
    }

    @Test
    fun `isFormValid returns true only if email and password are valid`() {
        viewModel.onEmailChange("valid@mail.com")
        viewModel.onPasswordChange("password123")
        assertTrue(viewModel.isFormValid)
    }

    @Test
    fun `loginEmailAndPassword emits loading then success`() = runTest {
        val flow =  flowOf(EcommerceResponse.Success(UserModel("test","image")))
        coEvery { loginUseCase.invoke("test@gmail.com", "password123") } returns flow
        coEvery { authAnalytics.trackLoginButtonClicked() } just Runs
        coEvery { authAnalytics.trackLoginSuccess("test","test@gmail.com") } just Runs

        val job = launch {
            viewModel.eventFlow.test {
                assertEquals(
                    LoginEvent.ShowSnackbar("Login is Success"),
                    awaitItem()
                )
                cancel()
            }
        }

        viewModel.onEmailChange("test@gmail.com")
        viewModel.onPasswordChange("password123")
        viewModel.loginEmailAndPassword()

        advanceUntilIdle()
        job.join()

        viewModel.uiState.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertTrue(state.isSuccess)
        }

        coVerify { authAnalytics.trackLoginButtonClicked() }
        coVerify { authAnalytics.trackLoginSuccess("test","test@gmail.com") }

    }

    @Test
    fun `loginEmailAndPassword emits failure`() = runTest {
        // Given
        val flow = flowOf(EcommerceResponse.Failure(400,"Error"))
        coEvery { loginUseCase.invoke("test@gmail.com","password123") } returns flow
        coEvery { authAnalytics.trackLoginButtonClicked() } just Runs
        coEvery { authAnalytics.trackLoginFailure("Error","test@gmail.com") } just Runs

        val job = launch {
            viewModel.eventFlow.test {
                assertEquals(
                    LoginEvent.ShowSnackbar("Error"),
                    awaitItem()
                )
                cancel()
            }
        }

        // When
        viewModel.onEmailChange("test@gmail.com")
        viewModel.onPasswordChange("password123")
        viewModel.loginEmailAndPassword()

        advanceUntilIdle()
        job.join()

        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertFalse(state.isSuccess)
            assertFalse(state.isLoading)
        }

        coVerify { authAnalytics.trackLoginButtonClicked() }
        coVerify { authAnalytics.trackLoginFailure("Error","test@gmail.com") }

    }

}