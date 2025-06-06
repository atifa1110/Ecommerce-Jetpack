package com.example.ecommerceapp.screen.home

import com.example.ecommerceapp.repository.state.StateRepository
import javax.inject.Inject

class SetDarkStateUseCase @Inject constructor(
    private val stateRepository: StateRepository
) {
    suspend operator fun invoke(isDarkMode : Boolean) {
        return stateRepository.setDarkMode(isDarkMode)
    }
}
