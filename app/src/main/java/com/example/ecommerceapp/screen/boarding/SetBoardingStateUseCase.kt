package com.example.ecommerceapp.screen.boarding

import com.example.ecommerceapp.repository.state.StateRepository
import javax.inject.Inject

class SetBoardingUseCase @Inject constructor(
    private val stateRepository: StateRepository
) {
    suspend operator fun invoke(complete: Boolean) {
        return stateRepository.setBoarding(complete)
    }
}
