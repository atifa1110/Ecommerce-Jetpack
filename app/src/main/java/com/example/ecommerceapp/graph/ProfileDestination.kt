package com.example.ecommerceapp.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.ecommerceapp.screen.profile.ProfileRoute

object ProfileDestination : EcommerceNavigationDestination {
    override val route: String = "profile_route"
    override val destination: String = "profile_destination"
}

fun NavGraphBuilder.profileGraph(
    onNavigateToHome : () -> Unit,
) = composable(route = ProfileDestination.route) {
    ProfileRoute(
        onNavigateToHome = onNavigateToHome
    )
}