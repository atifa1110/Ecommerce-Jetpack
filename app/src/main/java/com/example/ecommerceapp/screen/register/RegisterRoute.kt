package com.example.ecommerceapp.screen.register

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.SnackbarHost
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
import com.example.ecommerceapp.ui.theme.EcommerceAppTheme

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

    RegisterScreen(
        uiState = uiState,
        userMessage = uiState.userMessage?:"",
        onEmailChanged = { viewModel.onEmailChange(it) },
        onPasswordChanged = { viewModel.onPasswordChange(it) },
        onNavigateToLogin = onNavigateToLogin,
        onRegisterClick = {
            keyboardController?.hide()
            viewModel.registerEmailAndPassword()
        },
        snackBarHostState = snackBarHostState,
        snackBarMessageShown = {viewModel.snackBarMessageShown()}
    )
}

@Composable
fun RegisterScreen(
    uiState: RegisterUiState,
    userMessage : String,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onRegisterClick: () -> Unit,
    onNavigateToLogin: () -> Unit,
    snackBarMessageShown : () -> Unit,
    snackBarHostState: SnackbarHostState
) {
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
            emailError = uiState.emailError?:"",
            password = uiState.password,
            passwordError = uiState.passwordError?:"",
            isLoading = uiState.isLoading,
            onEmailChanged = onEmailChanged,
            onPasswordChanged = onPasswordChanged,
            onNavigateToLogin = onNavigateToLogin,
            onRegisterClick = onRegisterClick
        )
    }

    if (userMessage.isNotBlank()) {
        LaunchedEffect(userMessage) {
            snackBarHostState.showSnackbar(userMessage)
            snackBarMessageShown() // Called after snackbar hides
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
    isLoading : Boolean,
    loadingContent: @Composable () -> Unit = {
        LoaderScreen(modifier = Modifier.fillMaxSize())
    },
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onRegisterClick: () -> Unit,
    onNavigateToLogin: () -> Unit
){
    if(isLoading){
        loadingContent()
    }else {
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
                enable = emailError.isEmpty() && passwordError.isEmpty(),
                buttonText = R.string.register
            )
            DividerButton(false)
            OutlinedButtonComponent(onClick = onNavigateToLogin, buttonText = R.string.login)
            TextTermCondition(false)
        }
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
            userMessage = "",
            onEmailChanged = {},
            onPasswordChanged = {},
            onRegisterClick = {},
            onNavigateToLogin = {},
            snackBarMessageShown = {},
            snackBarHostState = snackBarHostState,
        )
    }
}
