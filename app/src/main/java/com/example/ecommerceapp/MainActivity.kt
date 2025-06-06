package com.example.ecommerceapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.ecommerceapp.graph.EcommerceNavHost
import com.example.ecommerceapp.graph.SplashDestination
import com.example.ecommerceapp.screen.home.HomeViewModel
import com.example.ecommerceapp.ui.theme.EcommerceAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val darkModeEnabled = homeViewModel.isDarkMode.collectAsState(false)
            EcommerceAppTheme (darkTheme = darkModeEnabled.value){
                EcommerceNavHost(
                    navController = rememberNavController(),
                    startDestination = SplashDestination,
                    isDarkMode = darkModeEnabled.value,
                    onBackButtonClick = {},
                    onShowMessage = {},
                    onToggleTheme = { isDark ->
                        homeViewModel.toggleDarkMode(isDark)
                    }
                )
            }
        }
    }
}

