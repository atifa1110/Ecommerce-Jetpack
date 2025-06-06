package com.example.ecommerceapp.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.ecommerceapp.screen.transaction.TransactionRoute

object TransactionDestination : EcommerceNavigationDestination {
    override val route: String = "transaction_route"
    override val destination: String = "transaction_destination"
}

fun NavGraphBuilder.transactionGraph(
    onNavigateToStatus : () -> Unit
) = composable(route = TransactionDestination.route) {
    TransactionRoute(
        onNavigateToStatus = onNavigateToStatus
    )
}