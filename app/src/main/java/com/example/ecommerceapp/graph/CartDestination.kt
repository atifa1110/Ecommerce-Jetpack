package com.example.ecommerceapp.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.ecommerceapp.screen.cart.CartRoute

object CartDestination : EcommerceNavigationDestination {
    override val route: String = "cart_route"
    override val destination: String = "cart_destination"
}

fun NavGraphBuilder.cartGraph(
    onNavigateToBack : () -> Unit,
    onNavigateToCheckout : () -> Unit
) = composable(route = CartDestination.route) {
    CartRoute(
        onNavigateToBack = onNavigateToBack,
        onNavigateToCheckout = onNavigateToCheckout
    )
}