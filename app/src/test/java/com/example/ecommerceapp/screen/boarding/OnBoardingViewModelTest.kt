package com.example.ecommerceapp.screen.boarding

import com.example.core.domain.usecase.SetBoardingStateUseCase
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class OnBoardingViewModelTest {

    private lateinit var viewModel: OnBoardingViewModel
    private lateinit var setBoardingUseCase: SetBoardingStateUseCase

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        setBoardingUseCase =  mockk(relaxed = true)
        viewModel = OnBoardingViewModel(setBoardingUseCase)
    }

    @Test
    fun `setBoardingState should invoke use case with true`() = runTest {
        coEvery { setBoardingUseCase.invoke(true) } just Runs
        // When
        viewModel.setBoardingState(true)
        advanceUntilIdle()
        // Then
        coVerify { setBoardingUseCase.invoke(true) }
    }

    @Test
    fun `setBoardingState should invoke use case with false`() = runTest {
        coEvery { setBoardingUseCase.invoke(false) } just Runs
        // When
        viewModel.setBoardingState(false)
        advanceUntilIdle()
        // Then
        coVerify { setBoardingUseCase.invoke(false) }
    }
}
