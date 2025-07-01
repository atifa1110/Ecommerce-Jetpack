package com.example.ecommerceapp.screen.main

import android.content.res.Configuration
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ecommerceapp.components.MainTopAppBar
import com.example.ecommerceapp.components.NavigationBottomBar
import com.example.ecommerceapp.components.NavigationSideBar
import com.example.ecommerceapp.graph.BottomNavHost
import com.example.ecommerceapp.graph.MainLevelDestination
import com.example.ecommerceapp.graph.TransactionDestination
import com.example.ecommerceapp.ui.theme.EcommerceAppTheme

@Composable
fun MainRoute(
    startTab: String?,
    isDarkMode: Boolean,
    navigationType: NavigationType,
    onNavigateToLogin : () -> Unit,
    onNavigateToDetail: (String) -> Unit,
    onNavigateToCart:() -> Unit,
    onNavigateToStatus:() -> Unit,
    onNavigateToNotification : () -> Unit,
    onNavigateToModular : () -> Unit,
    onToggleTheme : (Boolean) -> Unit,
    viewModel: MainViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getCartSize()
        viewModel.getWishlistSize()
        viewModel.getNotificationSize()
    }

    MainScreen(
        startTab = startTab,
        cartSize = uiState.cartSize,
        favoriteSize = uiState.wishlistSize,
        notificationSize = uiState.notificationSize,
        userName = uiState.userName,
        userImage = uiState.userImage,
        isDarkMode = isDarkMode,
        onLogoutClick = onNavigateToLogin,
        onNavigateToDetail =onNavigateToDetail,
        onNavigateToStatus = onNavigateToStatus,
        onNavigateToCart = onNavigateToCart,
        onNavigateToNotification = onNavigateToNotification,
        onNavigateToModular = onNavigateToModular,
        onToggleTheme = onToggleTheme,
        navigationType = navigationType
    )
}

@Composable
fun MainScreen(
    startTab: String?,
    cartSize: Int,
    favoriteSize: Int,
    notificationSize: Int,
    userName: String,
    userImage : String,
    isDarkMode: Boolean,
    onLogoutClick: () -> Unit,
    onNavigateToDetail: (String) -> Unit,
    onNavigateToStatus: () -> Unit,
    onNavigateToCart: () -> Unit,
    onNavigateToNotification: () -> Unit,
    onNavigateToModular: () -> Unit,
    onToggleTheme : (Boolean) -> Unit,
    navigationType: NavigationType
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val topLevelDestinations = MainLevelDestination.entries.toTypedArray()
    val startDestination = startTab ?: MainLevelDestination.Home.route

    MainScaffoldLayout(
        navigationType = navigationType,
        items = topLevelDestinations,
        startDestination = startDestination,
        currentDestination = currentDestination,
        navController = navController,
        badgeFavorite = favoriteSize,
        badgeNotification = notificationSize,
        badgeCart = cartSize,
        userName = userName,
        userImage = userImage,
        isDarkMode = isDarkMode,
        onLogoutClick = onLogoutClick,
        onNavigateToDetail = onNavigateToDetail,
        onNavigateToStatus = onNavigateToStatus,
        onNavigateToCart = onNavigateToCart,
        onNavigateToNotification = onNavigateToNotification,
        onNavigateToModular = onNavigateToModular,
        onToggleTheme = onToggleTheme
    )
}

@Composable
fun MainScaffoldLayout(
    navigationType : NavigationType,
    items: Array<MainLevelDestination>,
    currentDestination: NavDestination?,
    startDestination : String,
    navController: NavHostController,
    badgeFavorite: Int,
    badgeNotification: Int,
    badgeCart: Int,
    userName: String,
    userImage : String,
    isDarkMode: Boolean,
    onLogoutClick: () -> Unit,
    onNavigateToDetail: (String) -> Unit,
    onNavigateToStatus: () -> Unit,
    onNavigateToCart: () -> Unit,
    onNavigateToNotification: () -> Unit,
    onNavigateToModular: () -> Unit,
    onToggleTheme : (Boolean) -> Unit,
) {
    val useRail = navigationType == NavigationType.NAV_RAIL

    Row (modifier = Modifier.fillMaxSize()) {
        if (useRail) {
            NavigationSideBar(
                items = items,
                currentDestination = currentDestination,
                navController = navController,
                badgeFavorite = badgeFavorite,
            )
        }

        Scaffold(
            modifier = Modifier.navigationBarsPadding(),
            topBar = {
                MainTopAppBar(
                    name = userName,
                    image = userImage,
                    badgeNotification = badgeNotification,
                    badgeCart = badgeCart,
                    onNavigateToNotification = onNavigateToNotification,
                    onNavigateToCart = onNavigateToCart,
                    onNavigateToModular = onNavigateToModular
                )
            },
            bottomBar = {
                if (!useRail) {
                    NavigationBottomBar(
                        items = items,
                        currentDestination = currentDestination,
                        navController = navController,
                        badgeFavorite = badgeFavorite
                    )
                }
            }
        ) {
            Column(modifier = Modifier.fillMaxSize().padding(it)
            ) {
                BottomNavHost(
                    navigationType = navigationType,
                    navController = navController,
                    startDestination = startDestination,
                    isDarkMode = isDarkMode,
                    onNavigateToDetail = onNavigateToDetail,
                    onLogoutClick = onLogoutClick,
                    onToggleTheme = onToggleTheme,
                    onNavigateToStatus = onNavigateToStatus
                )
            }
        }
    }
}


enum class NavigationType{
    BOTTOM_NAV,NAV_RAIL
}

@Preview("Light Mode", device = Devices.PIXEL_3)
@Preview("Dark Mode", device = Devices.PIXEL_3, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun MainBottomPreview() {
    EcommerceAppTheme {
        MainScreen(
            startTab = TransactionDestination.route,
            cartSize = 2,
            favoriteSize =2,
            notificationSize = 0,
            userName = "Test",
            userImage = "",
            isDarkMode = false,
            onLogoutClick = {},
            onNavigateToDetail = {},
            onNavigateToStatus = {},
            onNavigateToCart = {},
            onNavigateToNotification = {},
            onNavigateToModular = {},
            onToggleTheme = {},
            navigationType = NavigationType.BOTTOM_NAV
        )
    }
}

@Preview("Light Mode", device = Devices.TABLET)
@Preview("Dark Mode", device = Devices.TABLET, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun MainRailPreview() {
    EcommerceAppTheme {
        MainScreen(
            startTab = TransactionDestination.route,
            cartSize = 2,
            favoriteSize =2,
            notificationSize = 0,
            userName = "Test",
            userImage = "",
            isDarkMode = false,
            onLogoutClick = {},
            onNavigateToDetail = {},
            onNavigateToStatus = {},
            onNavigateToCart = {},
            onNavigateToNotification = {},
            onNavigateToModular = {},
            onToggleTheme = {},
            navigationType = NavigationType.NAV_RAIL
        )
    }
}
