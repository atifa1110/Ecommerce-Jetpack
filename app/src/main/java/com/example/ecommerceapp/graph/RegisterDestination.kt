package com.example.ecommerceapp.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.ecommerceapp.screen.register.RegisterRoute

object RegisterDestination : EcommerceNavigationDestination {
    override val route: String = "register_route"
    override val destination: String = "register_destination"
}

fun NavGraphBuilder.registerGraph(
    onNavigateToLogin : () -> Unit,
    onNavigateToProfile : () -> Unit
) = composable(route = RegisterDestination.route) {
    RegisterRoute (
        onNavigateToLogin = onNavigateToLogin,
        onNavigateToProfile = onNavigateToProfile
    )
}