package com.example.ecommerceapp.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.screen.ModularRoute

object ModularDestination : EcommerceNavigationDestination{
    override val route = "modular_route"
    override val destination = "modular_destination"
}

fun NavGraphBuilder.modularGraph(
    onNavigateToBack: () -> Unit,
) = composable(route = ModularDestination.route) {
    ModularRoute (
        onNavigateToBack = onNavigateToBack
    )
}