package com.example.ecommerceapp.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.ecommerceapp.screen.wishlist.WishlistRoute

object WishlistDestination : EcommerceNavigationDestination {
    override val route: String = "wishlist_route"
    override val destination: String = "wishlist_destination"
}

fun NavGraphBuilder.wishlistGraph(
) = composable(route = WishlistDestination.route) {
    WishlistRoute()
}