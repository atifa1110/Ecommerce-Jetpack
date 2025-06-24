package com.example.ecommerceapp.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.ecommerceapp.screen.status.StatusRoute


object StatusDestination : EcommerceNavigationDestination {
    override val route = "status_route"
    override val destination = "status_destination"
}

fun NavGraphBuilder.statusGraph(
    onNavigateToTransaction : () -> Unit,
    onBackButton : () -> Unit,
) = composable(route = StatusDestination.route) {

    StatusRoute(
        onNavigateToTransaction = onNavigateToTransaction,
        onBackButton = onBackButton,
    )
}