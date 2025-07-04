package com.example.ecommerceapp.screen.store

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.example.ecommerceapp.R
import com.example.ecommerceapp.components.BottomSheetFilter
import com.example.ecommerceapp.components.ErrorStateUI
import com.example.ecommerceapp.components.FilterComponent
import com.example.ecommerceapp.components.LoadingIndicator
import com.example.ecommerceapp.components.LoadingStateUI
import com.example.ecommerceapp.components.ProductCardGrid
import com.example.ecommerceapp.components.ProductCardList
import com.example.ecommerceapp.components.SearchComponent
import com.example.core.ui.model.Product
import com.example.ecommerceapp.screen.main.NavigationType

@Composable
fun StoreRoute(
    navigationType: NavigationType,
    onNavigateToDetail : (String) -> Unit,
    viewModel: StoreViewModel = hiltViewModel()
) {
    val categoryItems = stringArrayResource(id = R.array.category_items)
    val sortItems = stringArrayResource(id = R.array.sort_items)
    val uiState by viewModel.uiState.collectAsState()
    val products = viewModel.getProductsFilter.collectAsLazyPagingItems()
    val filters = uiState.productFilter.activeFilter

    StoreScreen(
        navigationType = navigationType,
        uiState = uiState,
        products = products,
        filters = filters,
        categoryItems = categoryItems,
        sortItems = sortItems,
        updateSelectedSort = {viewModel.updateSelectedSort(it)},
        updateSelectedBrand = {viewModel.updateSelectedBrand(it)},
        updatePriceLowest = {viewModel.updatePriceLowest(it)},
        updatePriceHighest = {viewModel.updatePriceHighest(it)},
        onSearchChange = {viewModel.searchQuery(it)},
        searchProduct = {viewModel.fetchSearchSuggestions()},
        setClickedGrid = {viewModel.toggleClickedGrid()},
        setBottomSheetOpen = {viewModel.setBottomSheetOpen(it)},
        setSearchScreenOpen = {viewModel.setSearchScreenOpen(it)},
        onSuggestionSelected = viewModel::onSuggestionSelected,
        onResetQuery = viewModel::resetQuery,
        onRefreshAnalytics = {},
        onRefreshToken = {
            viewModel.refreshToken{
                products.refresh()
            }
        },
        onSetQuery = {brand,lowest,highest,sort->
            viewModel.setQuery(brand,lowest,highest,sort)
        },
        onNavigateToDetail = onNavigateToDetail
    )
}

@Composable
fun StoreScreen(
    navigationType: NavigationType,
    uiState: StoreUiState,
    products: LazyPagingItems<Product>,
    filters : List<String>,
    categoryItems : Array<String>,
    sortItems : Array<String>,
    updateSelectedSort : (String) -> Unit,
    updateSelectedBrand : (String) -> Unit,
    updatePriceLowest : (String) -> Unit,
    updatePriceHighest : (String) -> Unit,
    onSearchChange : (String) -> Unit,
    searchProduct : () -> Unit,
    setClickedGrid : () -> Unit,
    setBottomSheetOpen : (Boolean) -> Unit,
    setSearchScreenOpen : (Boolean) -> Unit,
    onSuggestionSelected : (String) -> Unit,
    onResetQuery : () -> Unit,
    onRefreshAnalytics : () -> Unit,
    onRefreshToken : () -> Unit,
    onSetQuery: (brand: String?, low: String?, high: String?, sort: String?) -> Unit,
    onNavigateToDetail : (String) -> Unit,
) {
    if(uiState.isSearchOpen){
        Dialog(
            onDismissRequest = { setBottomSheetOpen(false) },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            SearchScreen(
                uiState = uiState.searchSuggestionState,
                search = uiState.productFilter.search?:"",
                onSearchChange = onSearchChange,
                searchProduct = searchProduct,
                setSearchScreenOpen = setSearchScreenOpen,
                onSuggestionSelected = onSuggestionSelected,
                errorMessage = uiState.userMessage
            )
        }
    }else{
        Column(modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize().padding(16.dp)) {

            SearchComponent(
                search = uiState.productFilter.search?:"",
                onSearchChanged = {},
                setSearchScreenOpen = { setSearchScreenOpen(it) }
            )

            when(val state = products.loadState.refresh) {
                is LoadState.Error -> {
                    // ❌ Show error
                    ErrorStateUI(
                        error = state.error,
                        onResetQuery = onResetQuery,
                        onRefreshAnalytics = onRefreshAnalytics,
                        onRefreshToken = onRefreshToken
                    )
                }
                LoadState.Loading -> {
                    // ⏳ Show loading
                    LoadingStateUI(
                        navigationType = navigationType,
                        isGrid = uiState.isClickedGrid
                    )
                }
                is LoadState.NotLoading -> {
                    // ✅ Show content
                    SuccessStateUI(
                        navigationType = navigationType,
                        isClickedGrid = uiState.isClickedGrid,
                        isBottomSheetOpen = uiState.isBottomSheetOpen,
                        products = products,
                        filters = filters,
                        categoryItems = categoryItems,
                        sortItems = sortItems,
                        selectedSort = uiState.selectedSort?:"",
                        selectedBrand = uiState.selectedBrand?:"",
                        lowestPrice = uiState.lowestPrice,
                        highestPrice = uiState.highestPrice,
                        setClickedGrid = setClickedGrid,
                        setBottomSheetOpen = setBottomSheetOpen,
                        updateSelectedSort = updateSelectedSort,
                        updateSelectedBrand = updateSelectedBrand,
                        updatePriceLowest = updatePriceLowest,
                        updatePriceHighest = updatePriceHighest,
                        onDetailClick = onNavigateToDetail,
                        onResetQuery = onResetQuery,
                        onSetQuery = onSetQuery,
                    )
                }
            }

        }
    }
}

@Composable
fun SuccessStateUI(
    navigationType: NavigationType,
    isClickedGrid: Boolean,
    isBottomSheetOpen : Boolean,
    products: LazyPagingItems<Product>,
    filters : List<String>,
    categoryItems : Array<String>,
    sortItems : Array<String>,
    selectedSort: String,
    selectedBrand: String,
    lowestPrice: String,
    highestPrice: String,
    setBottomSheetOpen : (Boolean) -> Unit,
    setClickedGrid : () -> Unit,
    updateSelectedSort : (String) -> Unit,
    updateSelectedBrand : (String) -> Unit,
    updatePriceLowest : (String) -> Unit,
    updatePriceHighest : (String) -> Unit,
    onDetailClick: (id: String) -> Unit,
    onResetQuery : () -> Unit,
    onSetQuery: (brand: String?, low: String?, high: String?, sort: String?) -> Unit,
) {

    FilterComponent(
        isClickedGrid = isClickedGrid,
        filters = filters,
        setBottomSheetOpen = setBottomSheetOpen,
        setClickedGrid = setClickedGrid
    )

    StoreProductList(
        isClickedGrid = isClickedGrid,
        products = products,
        onDetailClick = onDetailClick,
        navigationType = navigationType
    )

    BottomSheetFilter(
        selectedSort = selectedSort,
        selectedBrand = selectedBrand,
        lowestPrice = lowestPrice,
        highestPrice = highestPrice,
        categoryItems = categoryItems,
        sortItems = sortItems,
        isBottomSheetOpen = isBottomSheetOpen,
        setBottomSheetOpen = setBottomSheetOpen,
        updateSelectedBrand = updateSelectedBrand,
        updateSelectedSort = updateSelectedSort,
        updatePriceLowest = updatePriceLowest,
        updatePriceHighest = updatePriceHighest,
        onResetQuery = onResetQuery,
        onSetQuery = onSetQuery
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun StoreProductList(
    isClickedGrid: Boolean,
    products: LazyPagingItems<Product>,
    onDetailClick: (id: String) -> Unit,
    navigationType: NavigationType
) {
    var refreshing by remember { mutableStateOf(false) }
    val state = rememberPullRefreshState(refreshing, { products.refresh() })

    Box(modifier = Modifier.pullRefresh(state)) {
        if (isClickedGrid) {
            GridProductList(products = products, onDetailClick = onDetailClick, navigationType = navigationType)
        } else {
            ListProductList(products = products, onDetailClick = onDetailClick)
        }

        PullRefreshIndicator(
            refreshing = refreshing,
            state = state,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}

@Composable
fun GridProductList(
    products: LazyPagingItems<Product>,
    onDetailClick: (id: String) -> Unit,
    navigationType: NavigationType
) {
    val gridCells = when (navigationType) {
        NavigationType.NAV_RAIL -> GridCells.Adaptive(minSize = 180.dp)
        NavigationType.BOTTOM_NAV -> GridCells.Fixed(2)
    }

    LazyVerticalGrid(
        columns = gridCells,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            count = products.itemCount,
            key = products.itemKey { it.productId }
        ) { index ->
            products[index]?.let { product ->
                ProductCardGrid (product = product) {
                    onDetailClick(product.productId)
                }
            }
        }

        if (products.loadState.append is LoadState.Loading) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                LoadingIndicator()
            }
        }
    }
}

@Composable
fun ListProductList(
    products: LazyPagingItems<Product>,
    onDetailClick: (id: String) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            count = products.itemCount,
            key = products.itemKey { it.productId }
        ) { index ->
            products[index]?.let { product ->
                ProductCardList (
                    product = product,
                    onClickCard = { onDetailClick(product.productId) }
                )
            }
        }

        if (products.loadState.append is LoadState.Loading) {
            item {
                LoadingIndicator()
            }
        }
    }
}
