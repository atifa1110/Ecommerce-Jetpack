package com.example.ecommerceapp.screen.boarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.usecase.SetBoardingStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val setBoardingUseCase: SetBoardingStateUseCase
): ViewModel(){

    fun setBoardingState(complete : Boolean){
        viewModelScope.launch {
            setBoardingUseCase.invoke(complete)
        }
    }

}