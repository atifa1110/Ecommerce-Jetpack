package com.example.ecommerceapp.screen.wishlist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ecommerceapp.R
import com.example.ecommerceapp.components.ErrorPage
import com.example.ecommerceapp.components.LoaderScreen
import com.example.ecommerceapp.components.WishlistCardGrid
import com.example.ecommerceapp.components.WishlistCardList
import kotlinx.coroutines.flow.collectLatest

@Composable
fun WishlistRoute(
    viewModel: WishlistViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }

    // This will reload wishlist every time the screen is shown
    LaunchedEffect(Unit) {
        viewModel.getWishlist()
    }

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is WishlistEvent.ShowSnackbar -> {
                    snackBarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    WishlistScreen(
        uiState = uiState,
        snackBarHostState = snackBarHostState,
        deleteWishlist = viewModel::deleteWishlist,
        setClickedGrid = viewModel::setClickedGrid,
        addToCart = viewModel::addToCart
    )
}

@Composable
fun WishlistScreen(
    uiState: WishlistUiState,
    snackBarHostState : SnackbarHostState,
    deleteWishlist : (String) -> Unit,
    setClickedGrid : () -> Unit,
    addToCart : (String) -> Unit
) {
    Scaffold(
        snackbarHost = {SnackbarHost(hostState = snackBarHostState)},
    ) {
        WishlistContent(
            modifier = Modifier.padding(it),
            isClickedGrid = uiState.isClickedGrid,
            uiState = uiState,
            deleteWishlist = deleteWishlist,
            setClickedGrid = setClickedGrid,
            addToCart =addToCart
        )
    }
}

@Composable
fun WishlistContent(
    modifier: Modifier,
    isClickedGrid : Boolean,
    uiState: WishlistUiState,
    deleteWishlist : (String) -> Unit,
    setClickedGrid : () -> Unit,
    addToCart : (String) -> Unit,
    loadingContent: @Composable () -> Unit = {
        LoaderScreen(modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background))
    },
) {
    if (uiState.isLoading) {
        loadingContent()
    } else if (uiState.isError || uiState.wishlists.isEmpty()) {
        ErrorPage(
            title = stringResource(id = R.string.empty),
            message = stringResource(id = R.string.resource),
            button = stringResource(R.string.refresh),
            onButtonClick = {},
            alpha = 0F
        )
    } else {
        Column(
            modifier.background(MaterialTheme.colorScheme.background)
                .fillMaxSize().padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "${uiState.wishlists.size} " + stringResource(id = R.string.item),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W400
                    )
                }

                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    Spacer(
                        modifier = Modifier
                            .height(24.dp)
                            .width(1.dp)
                            .background(Color.Gray)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Icon(
                        modifier = Modifier.clickable { setClickedGrid() },
                        imageVector = if (isClickedGrid) {
                            Icons.Default.GridView
                        } else Icons.AutoMirrored.Filled.FormatListBulleted,
                        contentDescription = "List"
                    )
                }
            }

            if (isClickedGrid) {
                LazyVerticalGrid(GridCells.Fixed(2)) {
                    items(uiState.wishlists) { item ->
                        WishlistCardGrid(
                            wishlist = item,
                            onDeleteFavorite = { deleteWishlist(it) },
                            onAddToCart = {addToCart(item.productId)}
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    items(uiState.wishlists) { item ->
                        WishlistCardList(
                            wishlist = item,
                            onDeleteFavorite = { deleteWishlist(it) },
                            onAddToCart = {addToCart(item.productId)}
                        )
                    }
                }
            }
        }
    }
}

