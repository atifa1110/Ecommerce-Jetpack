package com.example.ecommerceapp.graph

import androidx.compose.runtime.livedata.observeAsState

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.ecommerceapp.screen.main.MainRoute

object MainDestination : EcommerceNavigationDestination {
    override val route: String = "main_route"
    override val destination: String = "main_destination"

    const val startTabArg = "startTab"
    val routeWithArgs = "$route?$startTabArg={$startTabArg}"

    fun createRouteWithTab(tabRoute: String): String {
        return "$route?$startTabArg=$tabRoute"
    }
}

fun NavGraphBuilder.mainGraph(
    isDarkMode: Boolean,
    onNavigateToLogin : () -> Unit,
    onToggleTheme : (Boolean) -> Unit,
    onNavigateToDetail: (String) -> Unit,
    onNavigateToCart:() -> Unit,
    onNavigateToStatus:() -> Unit,
    onNavigateToNotification : () -> Unit,
    onNavigateToModular : () -> Unit
) = composable(
    route = MainDestination.routeWithArgs,
    arguments = listOf(
        navArgument(MainDestination.startTabArg) {
            type = NavType.StringType
            defaultValue = MainLevelDestination.Home.route // Default jika tidak ada
            nullable = true // Karena default sudah ada
    })
){ navBackStackEntry ->
    val startTab = navBackStackEntry.arguments?.getString(MainDestination.startTabArg)

    MainRoute(
        startTab = startTab,
        isDarkMode = isDarkMode,
        onNavigateToLogin = onNavigateToLogin,
        onNavigateToDetail = onNavigateToDetail,
        onNavigateToCart = onNavigateToCart,
        onNavigateToStatus = onNavigateToStatus,
        onNavigateToNotification= onNavigateToNotification,
        onNavigateToModular = onNavigateToModular,
        onToggleTheme = onToggleTheme
    )
}