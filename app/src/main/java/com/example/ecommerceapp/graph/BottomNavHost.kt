package com.example.ecommerceapp.graph

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

@Composable
fun BottomNavHost(
    navController: NavHostController,
    startDestination: String,
    isDarkMode : Boolean,
    onNavigateToDetail : (String) -> Unit,
    onNavigateToStatus : () -> Unit,
    onLogoutClick : () -> Unit,
    onToggleTheme : (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        homeGraph(
            isDarkMode = isDarkMode,
            onLogoutClick = onLogoutClick,
            onToggleTheme = onToggleTheme
        )
        storeGraph(
            onNavigateToDetail = onNavigateToDetail
        )
        transactionGraph(
            onNavigateToStatus = onNavigateToStatus
        )
        wishlistGraph()
    }
}