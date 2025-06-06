package com.example.ecommerceapp.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.ecommerceapp.screen.home.HomeRoute
import com.example.ecommerceapp.screen.splash.SplashRoute

object SplashDestination : EcommerceNavigationDestination {
    override val route: String = "splash_route"
    override val destination: String = "splash_destination"
}

fun NavGraphBuilder.splashGraph(
    onNavigateToBoarding : () -> Unit,
    onNavigateToLogin : () -> Unit,
    onNavigateToProfile : () -> Unit,
    onNavigateToHome : () -> Unit,
) = composable(route = SplashDestination.route) {
    SplashRoute(
        onNavigateToBoarding = onNavigateToBoarding,
        onNavigateToLogin = onNavigateToLogin,
        onNavigateToProfile =onNavigateToProfile,
        onNavigateToHome =onNavigateToHome
    )
}