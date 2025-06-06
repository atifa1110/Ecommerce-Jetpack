package com.example.ecommerceapp.graph

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.ecommerceapp.screen.detail.DetailRoute

object DetailsDestination : EcommerceNavigationDestination {
    override val route = "detail_route"
    override val destination = "detail_destination"

    const val idArgument = "id"
    private const val IdNullMessage = "Id is null."

    // Route with argument placeholder
    val routeWithArguments = "$route/{$idArgument}"

    // Use this to build a route when navigating
    fun createNavigationRoute(id: String): String {
        return "$route/$id"
    }

    // Extract the ID from SavedStateHandle
    fun fromSavedStateHandle(savedStateHandle: SavedStateHandle): String {
        return checkNotNull(savedStateHandle[idArgument]) { IdNullMessage }
    }
}

fun NavGraphBuilder.detailsGraph(
    onNavigateToCheckout : () -> Unit,
    onNavigateToReview : (String) -> Unit,
    onBackButtonClick: () -> Unit,
    onShowMessage: (String) -> Unit,
) = composable(
    route = DetailsDestination.routeWithArguments,
    arguments = listOf(
        navArgument(DetailsDestination.idArgument) { type = NavType.StringType },
    )
) {
    DetailRoute(
        onNavigateToCheckout = onNavigateToCheckout,
        onNavigateToReview = onNavigateToReview
    )
}