package com.example.ecommerceapp.screen.splash

import android.content.res.Configuration
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ecommerceapp.ui.theme.EcommerceAppTheme
import kotlinx.coroutines.delay
import com.example.ecommerceapp.R

@Composable
fun SplashRoute(
    onNavigateToBoarding : () -> Unit,
    onNavigateToLogin : () -> Unit,
    onNavigateToProfile : () -> Unit,
    onNavigateToHome : () -> Unit,
    viewModel: SplashViewModel = hiltViewModel()
) {
    var startAnimation by remember { mutableStateOf(false) }
    val alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 2000
        ), label = ""
    )

    val isBoarding by viewModel.onBoardingState.collectAsStateWithLifecycle(false)
    val isLogin by viewModel.onLoginState.collectAsStateWithLifecycle(false)
    val isProfile by viewModel.onProfileState.collectAsStateWithLifecycle(false)
    val isRegister by viewModel.onRegisterStateUseCase.collectAsStateWithLifecycle(false)

    LaunchedEffect(true) {
        startAnimation = true
        //viewModel.clearUserData()
        delay(3000)
        //onNavigateToHome()
        when{
            !isBoarding -> onNavigateToBoarding() //show onboarding if not completed
            //!isRegister -> onNavigateToLogin() //show login if register not completed
            isRegister && !isProfile -> onNavigateToProfile()//Registered but profile is not completed
            !isLogin -> onNavigateToLogin()// everything all good
            else -> onNavigateToHome() // profile completed go to login
        }
    }

    SplashScreen(alpha = alphaAnim.value)
}

@Composable
fun SplashScreen(alpha : Float) {
    Column (modifier = Modifier.fillMaxSize()
        .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Image(
            modifier = Modifier
                .size(138.dp, 138.dp)
                .alpha(alpha = alpha),
            painter = painterResource(R.drawable.logo),
            contentDescription = "Logo"
        )
    }
}

@Preview("Light Mode", device = Devices.PIXEL_3)
@Preview("Dark Mode", device = Devices.PIXEL_3, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SplashPreview() {
    EcommerceAppTheme {
        SplashScreen(alpha = 1f)
    }
}