package com.example.ecommerceapp.components

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Reorder
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ecommerceapp.ui.theme.poppins

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CenterTopAppBar(
    @StringRes titleResId: Int
) {
    Column {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = stringResource(id = titleResId),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.background
            )
        )
        HorizontalDivider()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopAppBar(
    name: String,
    image: String,
    badgeNotification: Int,
    badgeCart: Int,
    onNavigateToNotification: () -> Unit,
    onNavigateToCart: () -> Unit,
    onNavigateToModular: () -> Unit
) {
    Column {
        TopAppBar(
            title = {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.background
            ),
            navigationIcon = {
                IconButton(onClick = { /* your logic */ }) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "account_profile",
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                    )
                }
            },
            actions = {
                IconWithBadge(
                    icon = Icons.Default.Notifications,
                    contentDescription = "Notifications",
                    badgeCount = badgeNotification,
                    onClick = onNavigateToNotification
                )
                IconWithBadge(
                    icon = Icons.Default.ShoppingCart,
                    contentDescription = "Cart",
                    badgeCount = badgeCart,
                    onClick = onNavigateToCart
                )
                IconButton(onClick = { onNavigateToModular() }) {
                    Icon(Icons.Filled.Reorder, contentDescription = "Menu")
                }
            }
        )
        HorizontalDivider()
    }
}


@Composable
fun IconWithBadge(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    contentDescription: String,
    badgeCount: Int,
    maxCount: Int = 99,
    onClick: () -> Unit
) {
    val displayText = if (badgeCount > maxCount) "$maxCount+" else badgeCount.toString()
    val isSingleChar = displayText.length == 1

    IconButton(
        modifier = Modifier.size(50.dp),
        onClick = onClick
    ) {
        BadgedBox(
            badge = {
                if (badgeCount > 0) {
                    Badge(
                        modifier = if (isSingleChar) {
                            modifier.size(18.dp)
                        } else {
                            modifier.defaultMinSize(minWidth = 18.dp, minHeight = 18.dp)
                        },
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = Color.White
                    ) {
                        Text(
                            text = displayText,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.labelSmall,
                            maxLines = 1
                        )
                    }
                }
            }
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackTopAppBar(
    @StringRes titleResId: Int,
    onNavigateToBack : () -> Unit
){
    Column {
        TopAppBar(
            title = {
                Text(
                    stringResource(id = titleResId),
                    style = MaterialTheme.typography.titleLarge,
                )
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.background,
                titleContentColor = MaterialTheme.colorScheme.onBackground
            ),
            navigationIcon = {
                IconButton(onClick = { onNavigateToBack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "back_button",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        )
        HorizontalDivider()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackCenterTopAppBar(
    @StringRes titleResId: Int,
    onNavigateToBack : () -> Unit
) {
    Column {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    stringResource(id = titleResId),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.background),
            navigationIcon = {
                IconButton(onClick = { onNavigateToBack() }) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        "back button",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        )
        HorizontalDivider()
    }
}