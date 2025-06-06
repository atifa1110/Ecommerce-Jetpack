package com.example.ecommerceapp.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.ecommerceapp.screen.home.HomeRoute
import com.example.ecommerceapp.screen.store.StoreRoute

object StoreDestination : EcommerceNavigationDestination {
    override val route: String = "store_route"
    override val destination: String = "store_destination"
}

fun NavGraphBuilder.storeGraph(
    onNavigateToDetail: (String) -> Unit,
) = composable(route = StoreDestination.route) {
    StoreRoute(
        onNavigateToDetail = onNavigateToDetail
    )
}