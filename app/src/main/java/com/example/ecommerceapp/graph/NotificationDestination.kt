package com.example.ecommerceapp.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.ecommerceapp.screen.boarding.OnBoardingRoute
import com.example.ecommerceapp.screen.notification.NotificationRoute

object NotificationDestination : EcommerceNavigationDestination {
    override val route: String = "notification_route"
    override val destination: String = "notification_destination"
}

fun NavGraphBuilder.notificationGraph(
    isNotification : String?,
    onNavigationToMain : () -> Unit,
    onNavigationBack : () -> Unit,
) = composable(NotificationDestination.route){
    NotificationRoute(
        isNotification = isNotification,
        onNavigateToMain = onNavigationToMain,
        onNavigateBack = onNavigationBack
    )
}