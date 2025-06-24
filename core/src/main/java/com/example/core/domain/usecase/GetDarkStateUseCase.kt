package com.example.core.domain.usecase

import com.example.core.domain.repository.state.StateRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDarkStateUseCase @Inject constructor(
    private val stateRepository: StateRepository
) {
     operator fun invoke() : Flow<Boolean> {
        return stateRepository.getDarkMode()
    }
}