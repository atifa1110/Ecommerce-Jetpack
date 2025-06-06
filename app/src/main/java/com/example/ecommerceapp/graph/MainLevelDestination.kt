package com.example.ecommerceapp.graph

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Store
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.ecommerceapp.R

enum class MainLevelDestination(
    override val route: String,
    override val destination: String,
    val selectedIcon : ImageVector,
    val unselectedIcon : ImageVector,
    @StringRes val textResourceId: Int
) : EcommerceNavigationDestination {

    Home(
        route = HomeDestination.route,
        destination = HomeDestination.destination,
        selectedIcon = Icons.Default.Home,
        unselectedIcon = Icons.Filled.Home,
        textResourceId = R.string.home
    ),
    Store(
        route = StoreDestination.route,
        destination = StoreDestination.destination,
        selectedIcon = Icons.Default.Store,
        unselectedIcon = Icons.Filled.Store,
        textResourceId = R.string.store
    ),
    Wishlist(
        route = WishlistDestination.route,
        destination = WishlistDestination.destination,
        selectedIcon = Icons.Default.Favorite,
        unselectedIcon = Icons.Filled.Favorite,
        textResourceId = R.string.wishlist
    ),
    Transaction(
        route = TransactionDestination.route,
        destination = TransactionDestination.destination,
        selectedIcon = Icons.AutoMirrored.Default.ListAlt,
        unselectedIcon = Icons.AutoMirrored.Filled.ListAlt,
        textResourceId = R.string.transaction
    )
}