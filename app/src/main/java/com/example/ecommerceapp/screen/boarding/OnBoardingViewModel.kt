package com.example.ecommerceapp.screen.boarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val setBoardingUseCase: SetBoardingUseCase
): ViewModel(){

    fun setBoardingState(complete : Boolean){
        viewModelScope.launch {
            setBoardingUseCase.invoke(complete)
        }
    }

}