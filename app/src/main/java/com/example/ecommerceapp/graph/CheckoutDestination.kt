package com.example.ecommerceapp.graph

import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.ecommerceapp.screen.checkout.CheckoutRoute
import com.example.ecommerceapp.screen.checkout.CheckoutViewModel

object CheckoutDestination : EcommerceNavigationDestination {
    override val route = "checkout_route"
    override val destination = "checkout_destination"
}

fun NavGraphBuilder.checkoutGraph(
    onNavigateToStatus: () -> Unit,
) = composable(route = CheckoutDestination.route) {
    // ✅ Pass ViewModel to the route
    CheckoutRoute(
        onNavigateToStatus = onNavigateToStatus,
    )
}