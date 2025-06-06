package com.example.ecommerceapp.graph

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

@Composable
fun EcommerceNavHost (
    navController: NavHostController,
    startDestination: EcommerceNavigationDestination,
    isDarkMode : Boolean,
    onBackButtonClick: () -> Unit,
    onShowMessage: (String) -> Unit,
    onToggleTheme : (Boolean) -> Unit,
    modifier: Modifier = Modifier
){
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination.route,
        route = "root_graph"
    ) {
        splashGraph(
            onNavigateToLogin = {
                navController.navigate(LoginDestination.route) {
                    popUpTo(SplashDestination.route) { inclusive = true }
                }
            },
            onNavigateToProfile = {
                navController.navigate(ProfileDestination.route) {
                    popUpTo(SplashDestination.route) { inclusive = true }
                }
            },
            onNavigateToHome = {
                navController.navigate(MainDestination.route) {
                    popUpTo(SplashDestination.route) { inclusive = true }
                }
            },
            onNavigateToBoarding = {
                navController.navigate(OnBoardingDestination.route) {
                    popUpTo(SplashDestination.route) { inclusive = true }
                }
            }
        )

        boardingGraph (
            onNavigateToRegister = {
                navController.navigate(RegisterDestination.route)
            },
            onNavigateToLogin = {
                navController.navigate(LoginDestination.route)
            }
        )

        loginGraph (
            onNavigateToRegister = {navController.navigate(RegisterDestination.route)},
            onNavigateToHome = {navController.navigate(MainDestination.route)}
        )

        registerGraph(
            onNavigateToLogin = {navController.navigate(LoginDestination.route)},
            onNavigateToProfile = {navController.navigate(ProfileDestination.route)}
        )

        profileGraph(
            onNavigateToHome = {navController.navigate(MainDestination.route)}
        )

        mainGraph(
            isDarkMode = isDarkMode,
            onToggleTheme = onToggleTheme,
            onNavigateToDetail = {
                navController.navigate(DetailsDestination.createNavigationRoute(it))
            },
            onNavigateToLogin = {
                navController.navigate(LoginDestination.route) {
                    popUpTo(MainDestination.route) {
                        inclusive = true
                    }
                }
            },
            onNavigateToCart = {
                navController.navigate(CartDestination.route)
            },
            onNavigateToStatus = {
                navController.navigate(StatusDestination.route) {
                    popUpTo(MainDestination.route) {
                        inclusive = false
                    }
                }
            }
        )

        detailsGraph(
            onBackButtonClick = {},
            onShowMessage = {},
            onNavigateToCheckout = {
                navController.navigate(CheckoutDestination.route)
            },
            onNavigateToReview = {
                navController.navigate(ReviewDestination.createNavigationRoute(it))
            }
        )

        reviewGraph()

        cartGraph(
            onNavigateToCheckout = {
                navController.navigate(CheckoutDestination.route)
            },
            onNavigateToBack = {}
        )

        checkoutGraph(
            onNavigateToStatus = {
                navController.navigate(StatusDestination.route)
            },
        )

        statusGraph (
            onNavigateToTransaction = {
                navController.navigate(MainDestination.route) {
                    popUpTo(StatusDestination.route) { inclusive = true } // Or adjust depending on back stack
                }
            },
            onBackButton = {navController.popBackStack() },
        )
    }
}

