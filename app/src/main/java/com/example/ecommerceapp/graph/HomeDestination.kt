package com.example.ecommerceapp.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.ecommerceapp.screen.home.HomeRoute

object HomeDestination : EcommerceNavigationDestination {
    override val route: String = "home_route"
    override val destination: String = "home_destination"
}

fun NavGraphBuilder.homeGraph(
    isDarkMode : Boolean,
    onLogoutClick : () -> Unit,
    onToggleTheme : (Boolean) -> Unit,
) = composable(route = HomeDestination.route) {
    HomeRoute(
        isDarkMode = isDarkMode,
        onNavigateToLogin = onLogoutClick,
        onToggleTheme = onToggleTheme
    )
}