package com.example.ecommerceapp.screen.detail

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.ecommerceapp.R
import com.example.ecommerceapp.components.BackTopAppBar
import com.example.ecommerceapp.components.DotIndicatorWithScaling
import com.example.ecommerceapp.components.ErrorPage
import com.example.ecommerceapp.components.LoaderScreen
import com.example.core.ui.model.ProductDetail
import com.example.core.ui.model.ProductVariant
import com.example.ecommerceapp.screen.shared.SharedViewModel
import com.example.ecommerceapp.ui.theme.EcommerceAppTheme
import com.example.ecommerceapp.ui.theme.poppins
import com.example.ecommerceapp.utils.currency
import kotlinx.coroutines.flow.collectLatest

@Composable
fun DetailRoute(
    onNavigateToBack : () -> Unit,
    onNavigateToCheckout :() -> Unit,
    onNavigateToReview : (String) -> Unit,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val sharedViewModel: SharedViewModel = hiltViewModel(LocalContext.current as ComponentActivity)
    val uiState by viewModel.uiState.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val pagerState = rememberPagerState(initialPage = 0){uiState.productDetail.image?.size?:0}

    LaunchedEffect(Unit) {
        viewModel.loadDetailProduct()
    }

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is DetailEvent.ShowSnackbar -> {
                    snackBarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    DetailScreen(
        uiState = uiState,
        context = context,
        pagerState = pagerState,
        snackBarHostState = snackBarHostState,
        onVariantSelected = viewModel::onVariantSelected,
        onWishlistDetail = viewModel::onWishlistDetail,
        onAddToCart = viewModel::onAddToCart,
        onCheckout = {
            viewModel.checkoutAnalytics()
            sharedViewModel.setDetailCartItems(
                uiState.selectedVariant?: ProductVariant("",0),uiState.productDetail)
            onNavigateToCheckout()
        },
        onNavigateToReview = {onNavigateToReview(uiState.id)},
        onNavigateToBack = onNavigateToBack
    )

}

@Composable
fun DetailScreen(
    uiState: DetailUiState,
    context : Context,
    pagerState : PagerState,
    snackBarHostState: SnackbarHostState,
    loadingContent: @Composable () -> Unit = {
        LoaderScreen(modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background))
    },
    onVariantSelected : (ProductVariant) -> Unit,
    onWishlistDetail : () -> Unit,
    onAddToCart : () -> Unit,
    onCheckout : () -> Unit,
    onNavigateToReview : () -> Unit,
    onNavigateToBack : () -> Unit,
) {
    if(uiState.isLoading){
        loadingContent()
    }else {
        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
            topBar = {
                BackTopAppBar(titleResId = R.string.detail, onNavigateToBack = onNavigateToBack)
            },
            bottomBar = {
                if(!uiState.isError) {
                    HorizontalDivider()
                    Column(Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                OutlinedButton(
                                    modifier = Modifier.fillMaxWidth(),
                                    onClick = onCheckout,
                                ) {
                                    Text(
                                        text = stringResource(id = R.string.buy_now),
                                        style = MaterialTheme.typography.labelLarge,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Button(
                                    modifier = Modifier.fillMaxWidth(),
                                    onClick = { onAddToCart() },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary,
                                        contentColor = MaterialTheme.colorScheme.onPrimary
                                    ),
                                ) {
                                    Text(
                                        text = "+ " + stringResource(id = R.string.cart),
                                        style = MaterialTheme.typography.labelLarge
                                    )
                                }
                            }
                        }
                    }
                }
            }
        ) {
            DetailContent(
                isError = uiState.isError,
                isFavorite = uiState.productDetail.isWishlist,
                product = uiState.productDetail,
                selectedVariant = uiState.selectedVariant,
                totalPrice = uiState.totalPrice,
                context = context,
                pagerState = pagerState,
                onVariantSelected = onVariantSelected,
                onWishlistDetail = onWishlistDetail,
                onNavigateToReview = onNavigateToReview,
                modifier = Modifier.padding(it)
            )
        }
    }
}

@Composable
fun DetailContent(
    isError : Boolean,
    isFavorite : Boolean,
    product : ProductDetail,
    selectedVariant : ProductVariant?,
    totalPrice : Int,
    modifier : Modifier = Modifier,
    context : Context,
    pagerState: PagerState,
    onVariantSelected : (ProductVariant) -> Unit,
    onWishlistDetail : () -> Unit,
    onNavigateToReview : () -> Unit,
){
    if (isError){
        ErrorPage(
            title = stringResource(id = R.string.empty),
            message = stringResource(id = R.string.resource),
            button = stringResource(R.string.refresh),
            onClick = {},
            alpha = 0F
        )
    }else {
        Column(
            modifier = modifier.background(MaterialTheme.colorScheme.background)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            val pages = product.image

            Box(
                modifier = Modifier
                    .height(309.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.BottomCenter
            ) {
                HorizontalPager(
                    modifier = Modifier,
                    state = pagerState,
                    verticalAlignment = Alignment.CenterVertically
                ) { position ->
                    AsyncImage(
                        modifier = Modifier.fillMaxSize(),
                        model = pages?.get(position),
                        contentDescription = "Products image",
                        contentScale = ContentScale.FillBounds,
                        placeholder = painterResource(id = R.drawable.thumbnail),
                        error = painterResource(id = R.drawable.thumbnail)
                    )
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    DotIndicatorWithScaling(
                        pagerState = pagerState,
                        totalDots = pages?.size ?: 0,
                        selectedColor = MaterialTheme.colorScheme.primary,
                        unselectedColor = LightGray
                    )
                }
            }

            Column(Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = currency(totalPrice),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.W600,
                            fontFamily = poppins
                        )
                    }
                    Row(
                        Modifier.fillMaxWidth()
                            .weight(1f),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Icon(
                            modifier = Modifier.clickable {
                                val sendIntent: Intent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra("Product Name", product.productName)
                                    putExtra("Product Price", product.productPrice)
                                    putExtra(
                                        Intent.EXTRA_TEXT,
                                        "http://172.17.20.151:5000/products/${product.productId}"
                                    )
                                    flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                                    type = "text/plain"
                                }
                                val shareIntent = Intent.createChooser(sendIntent, null)
                                context.startActivity(shareIntent)
                            },
                            imageVector = Icons.Default.Share,
                            contentDescription = "Star"
                        )
                        Spacer(modifier = Modifier.width(16.dp))

                        Icon(
                            modifier = Modifier.clickable { onWishlistDetail() },
                            tint = if (isFavorite) Color.Red else MaterialTheme.colorScheme.onBackground,
                            imageVector = if (isFavorite) {
                                Icons.Filled.Favorite
                            } else {
                                Icons.Default.Favorite
                            },
                            contentDescription = null
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = product.productName.toString(),
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Text(
                        text = stringResource(id = R.string.sold) + " ${product.sale}",
                        style = MaterialTheme.typography.bodySmall
                    )

                    AssistChip(
                        modifier = Modifier.size(100.dp,30.dp),
                        onClick = {},
                        label = {
                            Text(
                                text = "${product.productRating} (${product.totalRating})",
                                style = MaterialTheme.typography.bodySmall
                            )
                        },
                        leadingIcon = {
                            Icon(
                                tint = Color(0xFFFFD700),
                                modifier = Modifier.size(18.dp),
                                imageVector = Icons.Filled.Star,
                                contentDescription = "Star"
                            )
                        },
                        shape = RoundedCornerShape(50)
                    )
                }
            }

            HorizontalDivider()

            Column(Modifier.padding(16.dp)) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.choose_variant),
                    style = MaterialTheme.typography.titleMedium
                )

                product.productVariant?.takeIf { it.isNotEmpty() }?.let { variants ->
                    LazyRow(modifier = Modifier.fillMaxWidth()) {
                        items(variants) { item ->
                            FilterChip(
                                modifier = Modifier.padding(end = 6.dp),
                                selected = (selectedVariant?.variantName == item.variantName),
                                onClick = {
                                    onVariantSelected(item)
                                },
                                label = {
                                    Text(
                                        text = item.variantName,
                                        style = MaterialTheme.typography.labelLarge,
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                                    labelColor = MaterialTheme.colorScheme.onBackground
                                )
                            )
                        }
                    }
                }
            }

            HorizontalDivider()

            Column(Modifier.padding(16.dp)) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.description),
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    text = product.description.toString(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            HorizontalDivider()

            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(
                            text = stringResource(id = R.string.buyer_review),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            onClick = {
                                onNavigateToReview()
                            }
                        ) {
                            Text(
                                text = stringResource(id = R.string.see_all),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }

                Row(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier,
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Star, contentDescription = "",
                            tint = Color(0xFFFFD700)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = product.productRating.toString(),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.W600,
                            fontFamily = poppins
                        )
                        Text(
                            text = " / 5.0",
                            style = MaterialTheme.typography.bodyMedium
                        )

                    }
                    Spacer(modifier = Modifier.width(16.dp))

                    Column(Modifier.fillMaxWidth()) {
                        Text(
                            text = "${product.totalSatisfaction}% " + stringResource(
                                id = R.string.buyer_satisfied
                            ),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W600,
                            fontFamily = poppins
                        )
                        Text(
                            text = "${product.totalRating} " + stringResource(id = R.string.rating) + " · " + "${product.totalReview} " + stringResource(
                                id = R.string.review
                            ),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

@Preview("Light Mode", device = Devices.PIXEL_3)
@Preview("Dark Mode", device = Devices.PIXEL_3, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DetailPreview() {
    EcommerceAppTheme {
        val snackBarHostState = remember { SnackbarHostState() }
        val context = LocalContext.current
        val pagerState = rememberPagerState(initialPage = 0){3}

        DetailScreen(
            uiState = DetailUiState(
                isLoading = false,
                selectedVariant = ProductVariant(
                    variantName = "RAM 32GB",
                    variantPrice = 1000000
                ),
                totalPrice = 37999000,
                productDetail = ProductDetail(
                    productName = "ASUS ROG Zephyrus M16 GU603ZE-I7R5G6T-O - Off Black",
                    productPrice = 27999000,
                    image = listOf(""),
                    brand = "Asus",
                    description = "description",
                    store = "AsusStore",
                    sale = 11,
                    stock = 1,
                    totalRating = 10,
                    totalReview = 4,
                    totalSatisfaction = 80,
                    productRating = 5.0f,
                    productVariant = listOf(
                        ProductVariant(
                            variantName = "RAM 16GB",
                            variantPrice = 0
                        ),
                        ProductVariant(
                            variantName = "RAM 32GB",
                            variantPrice = 1000000
                        )
                    ),
                    isWishlist = true
                ),

            ),
            context = context,
            pagerState = pagerState,
            snackBarHostState = snackBarHostState,
            onVariantSelected = {},
            onWishlistDetail = {},
            onAddToCart = {},
            onNavigateToReview = {},
            onCheckout = {},
            onNavigateToBack = {}
        )
    }
}
