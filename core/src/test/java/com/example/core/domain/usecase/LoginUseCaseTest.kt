package com.example.core.domain.usecase

import app.cash.turbine.test
import com.example.core.data.network.request.AuthRequest
import com.example.core.data.network.response.EcommerceResponse
import com.example.core.domain.model.UserModel
import com.example.core.domain.repository.auth.AuthRepository
import com.example.core.domain.repository.messaging.MessagingRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class LoginUseCaseTest {

    private lateinit var authRepository: AuthRepository
    private lateinit var messagingRepository: MessagingRepository
    private lateinit var loginUseCase: LoginUseCase

    @Before
    fun setUp() {
        authRepository = mockk()
        messagingRepository = mockk()
        loginUseCase = LoginUseCase(authRepository, messagingRepository)
    }

    @Test
    fun `invoke should emit Loading and Success`() = runTest {
        // Given
        val email = "test@email.com"
        val password = "password"
        val token = "firebase_token"
        val user = UserModel("test@email.com", "image.jpg")

        coEvery { messagingRepository.subscribeFcmTopic() } returns true
        coEvery { messagingRepository.getFirebasePhoneToken() } returns token
        coEvery {
            authRepository.login(AuthRequest(email, password, token))
        } returns flowOf(EcommerceResponse.Success(user))

        // When
        val result = loginUseCase(email, password)

        // Then
        result.test {
            val loading = awaitItem()
            assertEquals(EcommerceResponse.Loading,loading)
            val success = awaitItem()
            assertEquals(EcommerceResponse.Success(user), success)
            awaitComplete()
        }

        coVerify {
            messagingRepository.subscribeFcmTopic()
            messagingRepository.getFirebasePhoneToken()
            authRepository.login(AuthRequest(email, password, token))
        }
    }

    @Test
    fun `invoke should emit Loading and Failure`() = runTest {
        // Given
        val email = "test@email.com"
        val password = "password"
        val token = "firebase_token"
        val user = UserModel("test@email.com", "image.jpg")

        coEvery { messagingRepository.subscribeFcmTopic() } returns true
        coEvery { messagingRepository.getFirebasePhoneToken() } returns token
        coEvery {
            authRepository.login(AuthRequest(email, password, token))
        } returns flowOf(EcommerceResponse.Failure(400,"Failure"))

        // When
        val result = loginUseCase(email, password)

        // Then
        result.test {
            val loading = awaitItem()
            assertEquals(EcommerceResponse.Loading,loading)
            val failure = awaitItem()
            assertEquals(EcommerceResponse.Failure(400,"Failure"), failure)
            awaitComplete()
        }

        coVerify {
            messagingRepository.subscribeFcmTopic()
            messagingRepository.getFirebasePhoneToken()
            authRepository.login(AuthRequest(email, password, token))
        }
    }

    @Test
    fun `invoke should emit Failure on exception`() = runTest {
        // Given
        val email = "fail@email.com"
        val password = "password"

        coEvery { messagingRepository.subscribeFcmTopic() } returns false
        coEvery { messagingRepository.getFirebasePhoneToken() } throws RuntimeException("FCM Failed")

        // When
        val result = loginUseCase(email, password)

        // Then
        result.test {
            val loading = awaitItem()
            assertEquals(EcommerceResponse.Loading,loading)
            val failure = awaitItem()
            assertEquals(EcommerceResponse.Failure(-1,"FCM Failed"), failure)
            awaitComplete()
        }

        coVerify(exactly = 1) {
            messagingRepository.subscribeFcmTopic()
            messagingRepository.getFirebasePhoneToken()
        }
    }
}
