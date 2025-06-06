package com.example.ecommerceapp.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.ecommerceapp.screen.main.MainRoute

object MainDestination : EcommerceNavigationDestination {
    override val route: String = "main_route"
    override val destination: String = "main_destination"
}

fun NavGraphBuilder.mainGraph(
    isDarkMode: Boolean,
    onNavigateToLogin : () -> Unit,
    onToggleTheme : (Boolean) -> Unit,
    onNavigateToDetail: (String) -> Unit,
    onNavigateToCart:() -> Unit,
    onNavigateToStatus:() -> Unit
) = composable(MainDestination.route){
    MainRoute(
        isDarkMode = isDarkMode,
        onNavigateToLogin = onNavigateToLogin,
        onNavigateToDetail = onNavigateToDetail,
        onNavigateToCart = onNavigateToCart,
        onNavigateToStatus = onNavigateToStatus,
        onToggleTheme = onToggleTheme
    )
}