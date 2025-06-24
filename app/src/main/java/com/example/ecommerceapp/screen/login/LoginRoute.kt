package com.example.ecommerceapp.screen.login

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ecommerceapp.R
import com.example.ecommerceapp.components.ButtonComponent
import com.example.ecommerceapp.components.CenterTopAppBar
import com.example.ecommerceapp.components.DividerButton
import com.example.ecommerceapp.components.EmailComponent
import com.example.ecommerceapp.components.LoaderScreen
import com.example.ecommerceapp.components.OutlinedButtonComponent
import com.example.ecommerceapp.components.PasswordComponent
import com.example.ecommerceapp.components.TextTermCondition
import com.example.ecommerceapp.screen.register.RegisterEvent
import com.example.ecommerceapp.ui.theme.EcommerceAppTheme
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LoginRoute(
    onNavigateToHome: () -> Unit,
    onNavigateToRegister: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val uiState by viewModel.uiState.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current

    // 🔁 Navigate when login is successful
    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            viewModel.updateFcmToken()
            onNavigateToHome()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is LoginEvent.ShowSnackbar -> {
                    snackBarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    LoginScreen(
        uiState = uiState,
        isFormValid = viewModel.isFormValid,
        onEmailChanged = {viewModel.onEmailChange(it)},
        onPasswordChanged = {viewModel.onPasswordChange(it)},
        onLoginClick = {
            keyboardController?.hide()
            viewModel.loginEmailAndPassword()
        },
        onNavigateToRegister = onNavigateToRegister,
        snackBarHostState = snackBarHostState
    )
}

@Composable
fun LoginScreen(
    uiState: LoginUiState,
    isFormValid : Boolean,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onLoginClick : () -> Unit,
    onNavigateToRegister: () -> Unit,
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
                CenterTopAppBar(R.string.login)
            },
            snackbarHost = {
                SnackbarHost(hostState = snackBarHostState)
            },
        ) {
            LoginContent(
                modifier = Modifier.padding(it),
                email = uiState.email,
                emailError = uiState.emailError ?: "",
                password = uiState.password,
                passwordError = uiState.passwordError ?: "",
                isFormValid = isFormValid,
                onEmailChanged = onEmailChanged,
                onPasswordChanged = onPasswordChanged,
                onLoginClick = onLoginClick,
                onNavigateToRegister = onNavigateToRegister
            )
        }
    }
}

@Composable
fun LoginContent(
    modifier: Modifier = Modifier,
    email : String,
    emailError : String,
    password : String,
    passwordError : String,
    isFormValid : Boolean,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onLoginClick: () -> Unit,
    onNavigateToRegister: () -> Unit,
){
    Column(
        modifier = modifier.background(color = MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 32.dp),
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
            onClick = onLoginClick,
            enable = isFormValid,
            buttonText = R.string.login
        )
        DividerButton(true)
        OutlinedButtonComponent(onClick = onNavigateToRegister, buttonText = R.string.register)
        TextTermCondition(true)
    }
}

@Preview("Light Mode", device = Devices.PIXEL_3)
@Preview("Dark Mode", device = Devices.PIXEL_3, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun LoginPreview() {
    EcommerceAppTheme {
        val snackBarHostState = remember { SnackbarHostState() }
        LoginScreen (
            uiState = LoginUiState(
                email = "test@gmail.com",
                password = "12345678",
                passwordError = "",
                emailError = "",
                isLoading = false
            ),
            isFormValid = false,
            onEmailChanged = {},
            onPasswordChanged = {},
            onLoginClick = {},
            snackBarHostState = snackBarHostState,
            onNavigateToRegister = {}
        )
    }
}
