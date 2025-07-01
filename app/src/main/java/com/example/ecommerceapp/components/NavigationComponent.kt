package com.example.ecommerceapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.ecommerceapp.graph.HomeDestination
import com.example.ecommerceapp.graph.MainLevelDestination
import com.example.ecommerceapp.ui.theme.EcommerceAppTheme
import com.example.ecommerceapp.ui.theme.poppins

@Composable
fun NavigationSideBar(
    items: Array<MainLevelDestination>,
    currentDestination: NavDestination?,
    navController: NavHostController,
    badgeFavorite: Int
) {
    Row{
        NavigationRail(
            containerColor = MaterialTheme.colorScheme.background
        ) {
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically)
        ) {
            items.forEach { item ->
                NavigationRailItem(
                    selected = currentDestination?.hierarchy?.any {
                        it.route == item.route
                    } == true,
                    //screen.route == "home",
                    colors = NavigationRailItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        indicatorColor = MaterialTheme.colorScheme.secondaryContainer
                    ),
                    onClick = {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                inclusive = true
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = {
                        NavigationIcon(
                            item = item,
                            selected = currentDestination?.hierarchy?.any {
                                it.route == item.route
                            } == true,
                            badgeFavorite = badgeFavorite
                        )
                    },
                )
            }
        }
    }
        VerticalDivider()
    }
}

@Composable
fun NavigationBottomBar(
    items: Array<MainLevelDestination>,
    currentDestination: NavDestination?,
    navController: NavHostController,
    badgeFavorite: Int
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.background
    ) {
        items.forEach { item ->
            NavigationBarItem(
                label = {
                    Text(text = stringResource(item.textResourceId), style = MaterialTheme.typography.labelMedium)
                },
                selected = currentDestination?.hierarchy?.any {
                    it.route == item.route
                } == true,
                //item.route == HomeDestination.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    indicatorColor = MaterialTheme.colorScheme.secondaryContainer
                ),
                icon = {
                    NavigationIcon(
                        item = item,
                        selected = currentDestination?.hierarchy?.any {
                            it.route == item.route
                        } == true,
                        //item.route == HomeDestination.route,
                        badgeFavorite = badgeFavorite
                    )
                },
            )
        }
    }

}

@Composable
fun NavigationIcon(
    modifier: Modifier = Modifier,
    item: MainLevelDestination,
    selected: Boolean,
    badgeFavorite : Int,
    maxCount: Int = 99,
) {
    val displayText = if (badgeFavorite > maxCount) "$maxCount+" else badgeFavorite.toString()
    val isSingleChar = displayText.length == 1
    BadgedBox(
        badge = {
            if (item.selectedIcon == Icons.Default.Favorite) {
                if(badgeFavorite>0) {
                    Badge(
                        modifier = if (isSingleChar) {
                            modifier.size(18.dp)
                        } else {
                            modifier.defaultMinSize(minWidth = 18.dp, minHeight = 18.dp)
                        },
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = Color.White,
                    ) {
                        Text(
                            text = displayText,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
        }
    ) {
        Icon(
            imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
            contentDescription = item.name
        )
    }
}
