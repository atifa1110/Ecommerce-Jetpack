package com.example.ecommerceapp.graph

import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.ecommerceapp.screen.status.StatusRoute
import com.example.ecommerceapp.screen.status.StatusViewModel


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