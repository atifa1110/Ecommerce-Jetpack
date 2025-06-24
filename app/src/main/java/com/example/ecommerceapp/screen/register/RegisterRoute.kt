package com.example.ecommerceapp.screen.register

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ecommerceapp.R
import com.example.ecommerceapp.components.ButtonComponent
import com.example.ecommerceapp.components.CenterTopAppBar
import com.example.ecommerceapp.components.DividerButton
import com.example.ecommerceapp.components.EmailComponent
import com.example.ecommerceapp.components.LoaderScreen
import com.example.ecommerceapp.components.OutlinedButtonComponent
import com.example.ecommerceapp.components.PasswordComponent
import com.example.ecommerceapp.components.TextTermCondition
import com.example.ecommerceapp.screen.wishlist.WishlistEvent
import com.example.ecommerceapp.ui.theme.EcommerceAppTheme
import kotlinx.coroutines.flow.collectLatest

@Composable
fun RegisterRoute(
    onNavigateToProfile: () -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val keyboardController = LocalSoftwareKeyboardController.current

    // 🔁 Navigate when register is successful
    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onNavigateToProfile()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is RegisterEvent.ShowSnackbar -> {
                    snackBarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    RegisterScreen(
        uiState = uiState,
        isFormValid = viewModel.isFormValid,
        onEmailChanged = { viewModel.onEmailChange(it) },
        onPasswordChanged = { viewModel.onPasswordChange(it) },
        onNavigateToLogin = onNavigateToLogin,
        onRegisterClick = {
            keyboardController?.hide()
            viewModel.registerEmailAndPassword()
        },
        snackBarHostState = snackBarHostState,
    )
}

@Composable
fun RegisterScreen(
    uiState: RegisterUiState,
    isFormValid : Boolean,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onRegisterClick: () -> Unit,
    onNavigateToLogin: () -> Unit,
    loadingContent: @Composable () -> Unit = {
        LoaderScreen(modifier = Modifier.fillMaxSize())
    },
    snackBarHostState: SnackbarHostState
) {
    if(uiState.isLoading){
        loadingContent()
    }else {
        Scaffold(
            topBar = {
                CenterTopAppBar(R.string.register)
            },
            snackbarHost = {
                SnackbarHost(hostState = snackBarHostState)
            },
        ) {
            RegisterContent(
                modifier = Modifier.padding(it),
                email = uiState.email,
                emailError = uiState.emailError ?: "",
                password = uiState.password,
                passwordError = uiState.passwordError ?: "",
                isFormValid = isFormValid,
                onEmailChanged = onEmailChanged,
                onPasswordChanged = onPasswordChanged,
                onNavigateToLogin = onNavigateToLogin,
                onRegisterClick = onRegisterClick
            )
        }
    }
}

@Composable
fun RegisterContent(
    modifier: Modifier,
    email : String,
    emailError : String,
    password : String,
    passwordError : String,
    isFormValid : Boolean,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onRegisterClick: () -> Unit,
    onNavigateToLogin: () -> Unit
){
    Column(
        modifier = modifier.background(color = MaterialTheme.colorScheme.background)
            .fillMaxSize().padding(horizontal = 16.dp, vertical = 32.dp),
    ) {
        EmailComponent(email = email, emailError = emailError, onEmailChanged = onEmailChanged)
        Spacer(modifier = Modifier.height(16.dp))
        PasswordComponent(
            password = password,
            passwordError = passwordError,
            onPasswordChanged = onPasswordChanged
        )
        Spacer(modifier = Modifier.height(16.dp))
        ButtonComponent(
            onClick = onRegisterClick,
            enable = isFormValid,
            buttonText = R.string.register
        )
        DividerButton(false)
        OutlinedButtonComponent(onClick = onNavigateToLogin, buttonText = R.string.login)
        TextTermCondition(false)
    }
}

@Preview("Light Mode", device = Devices.PIXEL_3)
@Preview("Dark Mode", device = Devices.PIXEL_3, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun LoginPreview() {
    EcommerceAppTheme {
        val snackBarHostState = remember { SnackbarHostState() }
        RegisterScreen (
            uiState = RegisterUiState(
                email = "atifafiorenza24@gmail.com",
                emailError = "",
                password = "12345678",
                passwordError = "",
            ),
            isFormValid = false,
            onEmailChanged = {},
            onPasswordChanged = {},
            onRegisterClick = {},
            onNavigateToLogin = {},
            snackBarHostState = snackBarHostState,
        )
    }
}
