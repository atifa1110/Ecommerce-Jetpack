package com.example.ecommerceapp.graph

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.ecommerceapp.screen.review.ReviewRoute

object ReviewDestination : EcommerceNavigationDestination {
    override val route = "review_route"
    override val destination = "review_destination"

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

fun NavGraphBuilder.reviewGraph(
    onNavigateToBack: () -> Unit,
) = composable(
    route = ReviewDestination.routeWithArguments,
    arguments = listOf(
        navArgument(DetailsDestination.idArgument) { type = NavType.StringType },
    )
) {
    ReviewRoute(
        onNavigateToBack = onNavigateToBack
    )
}