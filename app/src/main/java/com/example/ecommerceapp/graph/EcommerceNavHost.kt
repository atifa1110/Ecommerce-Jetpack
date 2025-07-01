package com.example.ecommerceapp.graph

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.ecommerceapp.screen.main.NavigationType

@Composable
fun EcommerceNavHost (
    navController: NavHostController,
    navigationType: NavigationType,
    isNotification : String?,
    startDestination: String,
    isDarkMode : Boolean,
    onBackButtonClick: () -> Unit,
    onShowMessage: (String) -> Unit,
    onToggleTheme : (Boolean) -> Unit,
    modifier: Modifier = Modifier
){
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination,
    ) {
        boardingGraph (
            onNavigateToRegister = {
                navController.navigate(RegisterDestination.route){
                    popUpTo(OnBoardingDestination.route) {
                        inclusive = true
                    }
                }
            },
            onNavigateToLogin = {
                navController.navigate(LoginDestination.route){
                    popUpTo(OnBoardingDestination.route) {
                        inclusive = true
                    }
                }
            }
        )

        loginGraph (
            onNavigateToRegister = {
                navController.navigate(RegisterDestination.route)
            },
            onNavigateToHome = {
                navController.navigate(MainDestination.route)
            }
        )

        registerGraph(
            onNavigateToLogin = {navController.navigate(LoginDestination.route)},
            onNavigateToProfile = {navController.navigate(ProfileDestination.route)}
        )

        profileGraph(
            onNavigateToHome = {navController.navigate(MainDestination.route)}
        )

        mainGraph(
            navigationType = navigationType,
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
                navController.navigate(StatusDestination.route)
            },
            onNavigateToNotification = {
                navController.navigate(NotificationDestination.route) {
                    popUpTo(MainDestination.route) {
                        inclusive = false
                    }
                }
            },
            onNavigateToModular = {
                navController.navigate(ModularDestination.route) {
                    popUpTo(MainDestination.route) {
                        inclusive = false
                    }
                }
            }
        )

        detailsGraph(
            onBackButtonClick = onBackButtonClick,
            onShowMessage = {},
            onNavigateToCheckout = {
                navController.navigate(CheckoutDestination.route)
            },
            onNavigateToReview = {
                navController.navigate(ReviewDestination.createNavigationRoute(it))
            }
        )

        reviewGraph(
            onNavigateToBack = onBackButtonClick
        )

        cartGraph(
            onNavigateToCheckout = {
                navController.navigate(CheckoutDestination.route)
            },
            onNavigateToBack = onBackButtonClick
        )

        checkoutGraph(
            onNavigateToBack = onBackButtonClick,
            onNavigateToStatus = {
                navController.navigate(StatusDestination.route) {
                    popUpTo(CartDestination.route) { inclusive = true } // Remove checkout
                }
            },
            onNavigateToPayment = {
                navController.navigate(PaymentDestination.route)
            }
        )

        paymentGraph (
            onNavigateToBack = onBackButtonClick,
            onItemClick = { paymentItem ->
                navController
                    .previousBackStackEntry
                    ?.savedStateHandle
                    ?.set("payment", paymentItem)
                navController.popBackStack()
            }
        )

        statusGraph (
            onNavigateToTransaction = {
                //set bottom navigation to transaction
                navController.navigate(MainDestination.createRouteWithTab(MainLevelDestination.Transaction.route)) {
                    popUpTo(StatusDestination.route) { inclusive = true }
                }
            },
            onBackButton = {
                //set bottom navigation to transaction
                navController.navigate(MainDestination.createRouteWithTab(MainLevelDestination.Transaction.route)) {
                    popUpTo(StatusDestination.route) { inclusive = true }
                }
            },
        )

        notificationGraph(
            isNotification = isNotification,
            onNavigationToMain = {
                navController.navigate(MainDestination.route){
                    popUpTo(NotificationDestination.route){
                        inclusive = true
                    }
                }
            },
            onNavigationBack = onBackButtonClick
        )

        modularGraph(
            onNavigateToBack = onBackButtonClick
        )
    }
}

