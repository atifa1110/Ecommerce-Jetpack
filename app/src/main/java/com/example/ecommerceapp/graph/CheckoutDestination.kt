package com.example.ecommerceapp.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.core.ui.model.Payment
import com.example.ecommerceapp.screen.checkout.CheckoutRoute

object CheckoutDestination : EcommerceNavigationDestination {
    override val route = "checkout_route"
    override val destination = "checkout_destination"
}

fun NavGraphBuilder.checkoutGraph(
    onNavigateToBack: () -> Unit,
    onNavigateToStatus: () -> Unit,
    onNavigateToPayment : () -> Unit
) = composable(route = CheckoutDestination.route) { backStackEntry ->
    val paymentItem = backStackEntry.savedStateHandle.get<Payment.PaymentItem>("payment")
    // ✅ Pass ViewModel to the route
    CheckoutRoute(
        onNavigateToBack = onNavigateToBack,
        onNavigateToStatus = onNavigateToStatus,
        onNavigateToPayment = onNavigateToPayment,
        payment = paymentItem?: Payment.PaymentItem("","",false)
    )
}