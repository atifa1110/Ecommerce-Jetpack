package com.example.ecommerceapp

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.example.ecommerceapp.screen.splash.SplashViewModel
import com.example.ecommerceapp.components.PermissionDialog
import com.example.ecommerceapp.components.RationaleDialog
import com.example.ecommerceapp.graph.EcommerceNavHost
import com.example.ecommerceapp.graph.LoginDestination
import com.example.ecommerceapp.graph.MainDestination
import com.example.ecommerceapp.graph.NotificationDestination
import com.example.ecommerceapp.graph.OnBoardingDestination
import com.example.ecommerceapp.graph.ProfileDestination
import com.example.ecommerceapp.screen.main.NavigationType
import com.example.ecommerceapp.ui.theme.EcommerceAppTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import dagger.hilt.android.AndroidEntryPoint

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val splashViewModel : SplashViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        // Keep splash screen until loading finishes
        splashScreen.setKeepOnScreenCondition {
            !splashViewModel.isLoadingComplete.value
        }

        super.onCreate(savedInstanceState)

        val isNotification = intent.getStringExtra("navigate_to")

        setContent {
            val isBoarding by splashViewModel.onBoardingState.collectAsStateWithLifecycle(false)
            val isLogin by splashViewModel.onLoginState.collectAsStateWithLifecycle(false)
            val isProfile by splashViewModel.onProfileState.collectAsStateWithLifecycle(false)
            val isRegister by splashViewModel.onRegisterStateUseCase.collectAsStateWithLifecycle(false)
            val isLoadingComplete by splashViewModel.isLoadingComplete.collectAsStateWithLifecycle(false)
            val darkModeEnabled = splashViewModel.isDarkMode.collectAsState(false)

            // Determine start destination based on collected states
            val startDestination = when {
                // your conditions here
                !isBoarding -> OnBoardingDestination.route
                isRegister && !isProfile -> ProfileDestination.route
                !isLogin -> LoginDestination.route
                !isNotification.isNullOrEmpty() -> NotificationDestination.route
                else -> MainDestination.route
            }

            RequestNotificationPermissionDialog()
            if (isLoadingComplete) {
                val navController = rememberNavController()
                val windowSizeClass = calculateWindowSizeClass(this)
                val navigationType = when (windowSizeClass.widthSizeClass) {
                    WindowWidthSizeClass.Compact -> NavigationType.BOTTOM_NAV
                    WindowWidthSizeClass.Medium -> NavigationType.NAV_RAIL
                    WindowWidthSizeClass.Expanded -> NavigationType.NAV_RAIL
                    else -> NavigationType.BOTTOM_NAV
                }
                EcommerceAppTheme(darkTheme = darkModeEnabled.value) {
                    EcommerceNavHost(
                        navController = navController,
                        navigationType = navigationType,
                        isNotification = isNotification,
                        startDestination = startDestination,
                        isDarkMode = darkModeEnabled.value,
                        onBackButtonClick = { navController.popBackStack() },
                        onShowMessage = {},
                        onToggleTheme = { isDark ->
                            splashViewModel.toggleDarkMode(isDark)
                        }
                    )
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestNotificationPermissionDialog() {
    val permissionState =
        rememberPermissionState(permission = android.Manifest.permission.POST_NOTIFICATIONS)

    if (!permissionState.status.isGranted) {
        if (permissionState.status.shouldShowRationale) {
            RationaleDialog()
        } else {
            PermissionDialog { permissionState.launchPermissionRequest() }
        }
    }
}