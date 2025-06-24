package com.example.ecommerceapp.screen.profile

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.network.response.EcommerceResponse
import com.example.core.domain.usecase.ProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

data class ProfileUiState(
    val imageUri: Uri? = null,
    val showImageDialog: Boolean = false,
    val userImage: File? = null,
    val userName : String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val isError : Boolean = false
)

sealed class ProfileEvent {
    data class ShowSnackbar(val message: String) : ProfileEvent()
}

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileUseCase: ProfileUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState

    private val _eventFlow = MutableSharedFlow<ProfileEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun setImageUri(uri: Uri?) {
        _uiState.update { it.copy(imageUri = uri) }
    }

    fun setUserImage(file: File?) {
        _uiState.update { it.copy(userImage = file) }
    }

    fun setUserName(name: String) {
        _uiState.update { it.copy(userName = name) }
    }

    fun showDialog(isDialog : Boolean) {
        _uiState.update { it.copy(showImageDialog = isDialog) }
    }

    fun createImageFileAndUri(context: Context): Uri {
        val file = File.createTempFile(
            "profile-${System.currentTimeMillis()}",
            ".jpg",
            context.externalCacheDir
        )

        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )

        _uiState.update {
            it.copy(imageUri = uri, userImage = file)
        }

        return uri
    }

    fun onSetProfileClick() = viewModelScope.launch {
        val file = uiState.value.userImage
        val username = uiState.value.userName

        if (file == null || username.isBlank()) {
            // Handle error (e.g., show toast or snackbar)
            _eventFlow.emit(ProfileEvent.ShowSnackbar("Please input file and username correctly"))
            return@launch
        }

        val imagePart = MultipartBody.Part.createFormData(
            "userImage", file.name, file.asRequestBody("image/*".toMediaType())
        )

        val usernamePart = MultipartBody.Part.createFormData("userName", username)

        profileUseCase.invoke(imagePart, usernamePart).collect { result ->
            when (result) {
                is EcommerceResponse.Failure -> {
                    _uiState.update {
                        it.copy(isLoading = false, isSuccess = false, isError = true)
                    }
                    _eventFlow.emit(ProfileEvent.ShowSnackbar(result.error))
                }
                is EcommerceResponse.Loading -> {
                    _uiState.update {
                        it.copy(isLoading = true, isError = false, isSuccess = false)
                    }
                }
                is EcommerceResponse.Success -> {
                    _uiState.update {
                        it.copy(isLoading = false, isSuccess = true, isError = false)
                    }
                    _eventFlow.emit(ProfileEvent.ShowSnackbar("Upload is Success"))
                }
            }
        }
    }
}
