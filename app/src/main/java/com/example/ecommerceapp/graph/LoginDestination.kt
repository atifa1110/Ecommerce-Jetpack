package com.example.ecommerceapp.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.ecommerceapp.screen.login.LoginRoute

object LoginDestination : EcommerceNavigationDestination {
    override val route: String = "login_route"
    override val destination: String = "login_destination"
}

fun NavGraphBuilder.loginGraph(
    onNavigateToRegister : () -> Unit,
    onNavigateToHome : () -> Unit,
) = composable(route = LoginDestination.route) {
    LoginRoute(
        onNavigateToRegister = onNavigateToRegister,
        onNavigateToHome = onNavigateToHome
    )
}