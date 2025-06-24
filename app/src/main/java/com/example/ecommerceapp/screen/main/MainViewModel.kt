package com.example.ecommerceapp.screen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.usecase.GetCartSizeUseCase
import com.example.core.domain.usecase.GetNotificationSizeUseCase
import com.example.core.domain.usecase.GetUserNameUseCase
import com.example.core.domain.usecase.GetUserProfileUseCase
import com.example.core.domain.usecase.GetWishlistSizeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MainUiState(
    val userName : String = "",
    val userImage : String = "",
    val cartSize : Int = 0,
    val wishlistSize : Int = 0,
    val notificationSize : Int = 0
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val getUserNameUseCase: GetUserNameUseCase,
    private val getWishlistSizeUseCase: GetWishlistSizeUseCase,
    private val getCartSizeUseCase: GetCartSizeUseCase,
    private val getNotificationSizeUseCase: GetNotificationSizeUseCase
) : ViewModel(){
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState

    fun getCartSize() = viewModelScope.launch {
        val result = getCartSizeUseCase.invoke()
        _uiState.update {
            it.copy(cartSize = result)
        }
    }

    fun getWishlistSize() = viewModelScope.launch {
        val result = getWishlistSizeUseCase.invoke()
        _uiState.update {
            it.copy(wishlistSize = result)
        }
    }

    fun getNotificationSize() = viewModelScope.launch {
        val result = getNotificationSizeUseCase.invoke()
        _uiState.update {
            it.copy(notificationSize = result)
        }
    }


    init {
        loadUserData()
    }

    private fun loadUserData() = viewModelScope.launch {
        val name = getUserNameUseCase.invoke().firstOrNull()
        val profile = getUserProfileUseCase.invoke().firstOrNull()

        _uiState.update {
            it.copy(userName = name?:"Test", userImage = profile?:"")
        }
    }
}