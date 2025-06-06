package com.example.ecommerceapp.screen.home

import com.example.ecommerceapp.repository.state.StateRepository
import javax.inject.Inject

class ClearAppStateUseCase  @Inject constructor(
    private val stateRepository: StateRepository
) {
    suspend operator fun invoke() {
        return stateRepository.clearAppState()
    }
}
