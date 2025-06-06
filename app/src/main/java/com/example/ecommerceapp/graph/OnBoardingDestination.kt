package com.example.ecommerceapp.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.ecommerceapp.screen.boarding.OnBoardingRoute

object OnBoardingDestination : EcommerceNavigationDestination {
    override val route: String = "boarding_route"
    override val destination: String = "boarding_destination"
}

fun NavGraphBuilder.boardingGraph(
    onNavigateToLogin : () -> Unit,
    onNavigateToRegister : () -> Unit,
) = composable(OnBoardingDestination.route){
    OnBoardingRoute(
        onNavigateToLogin = onNavigateToLogin,
        onNavigateToRegister = onNavigateToRegister
    )
}