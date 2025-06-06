package com.example.ecommerceapp.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.ecommerceapp.data.ui.Payment
import com.example.ecommerceapp.screen.payment.PaymentRoute

object PaymentDestination : EcommerceNavigationDestination {
    override val route: String = "payment_route"
    override val destination: String = "payment_destination"
}

fun NavGraphBuilder.paymentGraph(
    onNavigateToBack : () -> Unit,
    onItemClick : (Payment.PaymentItem) -> Unit,
) = composable(route = PaymentDestination.route) {
    PaymentRoute(
        onNavigateToBack = onNavigateToBack,
        onItemClick = onItemClick
    )
}
