package com.example.ecommerceapp.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.usecase.ClearAppStateUseCase
import com.example.core.domain.usecase.ClearAuthTokenUseCase
import com.example.core.domain.usecase.ClearUserDataUseCase
import com.example.core.domain.usecase.GetLanguageCodeStateUseCase
import com.example.core.domain.usecase.SetLanguageCodeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val clearAuthTokenUseCase: ClearAuthTokenUseCase,
    private val clearAppStateUseCase: ClearAppStateUseCase,
    private val clearUserDataUseCase: ClearUserDataUseCase,
    private val getLanguageCodeStateUseCase: GetLanguageCodeStateUseCase,
    private val setLanguageCodeUseCase: SetLanguageCodeUseCase
) : ViewModel(){

    fun onLogoutClick () = viewModelScope.launch {
        clearAppStateUseCase.invoke()
        clearUserDataUseCase.invoke()
        clearAuthTokenUseCase.invoke()
    }

    val languageCode: StateFlow<String> = getLanguageCodeStateUseCase.invoke()
        .stateIn(viewModelScope, SharingStarted.Eagerly, "en")

    fun setLanguage(code: String) {
        viewModelScope.launch {
            setLanguageCodeUseCase.invoke(code)
        }
    }

}