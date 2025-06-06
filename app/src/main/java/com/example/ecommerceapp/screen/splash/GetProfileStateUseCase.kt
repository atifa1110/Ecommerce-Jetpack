package com.example.ecommerceapp.screen.splash

import com.example.ecommerceapp.repository.state.StateRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProfileStateUseCase @Inject constructor(
    private val stateRepository: StateRepository
) {
    operator fun invoke() : Flow<Boolean> {
        return stateRepository.getProfile()
    }
}
