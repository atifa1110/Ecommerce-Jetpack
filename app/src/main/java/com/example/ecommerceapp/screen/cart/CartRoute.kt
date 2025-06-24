package com.example.ecommerceapp.screen.cart

import android.content.res.Configuration
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ecommerceapp.R
import com.example.ecommerceapp.components.BackTopAppBar
import com.example.ecommerceapp.components.CartListCard
import com.example.ecommerceapp.components.ErrorPage
import com.example.ecommerceapp.components.LoaderScreen
import com.example.core.ui.model.Cart
import com.example.ecommerceapp.screen.shared.SharedViewModel
import com.example.ecommerceapp.ui.theme.EcommerceAppTheme
import com.example.ecommerceapp.utils.currency

@Composable
fun CartRoute(
    onNavigateToBack: () -> Unit,
    onNavigateToCheckout: () -> Unit,
    viewModel: CartViewModel = hiltViewModel(),
) {
    val sharedViewModel: SharedViewModel = hiltViewModel(LocalContext.current as ComponentActivity)
    val uiState by viewModel.uiState.collectAsState()
    val isAllChecked by viewModel.isAllChecked.collectAsState()
    val isAnyChecked by viewModel.isAnyItemChecked.collectAsState()
    val totalCheckedPrice by viewModel.totalCheckedPrice.collectAsState()
    val checkedCarts by viewModel.checkedCarts.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadCartItems()
    }

    CartScreen(
        uiState = uiState,
        onNavigateToBack = onNavigateToBack,
        isButtonVisible = isAnyChecked,
        isAllChecked = isAllChecked,
        selectAll = {viewModel.setAllChecked(true)},
        clearSelection = {viewModel.setAllChecked(false)},
        toggleItemCheck = viewModel::toggleItemChecked,
        totalCheckedPrice = totalCheckedPrice,
        increaseQuantity = { viewModel.updateQuantity(it,true) },
        decreaseQuantity = { viewModel.updateQuantity(it,false) },
        deleteCheckedItem = viewModel::deleteCheckedItems,
        deleteCartItem = viewModel::deleteCartItem,
        setCheckedCartItems = {
            sharedViewModel.setCheckedCartItems(checkedCarts)
            onNavigateToCheckout()
        }
    )
}

@Composable
fun CartScreen(
    uiState: CartUiState,
    onNavigateToBack: () -> Unit,
    isButtonVisible: Boolean,
    isAllChecked : Boolean,
    totalCheckedPrice: Int,
    selectAll : () -> Unit,
    clearSelection : () -> Unit,
    toggleItemCheck : (id: String) -> Unit,
    increaseQuantity : (String) -> Unit,
    decreaseQuantity : (String) -> Unit,
    deleteCheckedItem : () -> Unit,
    deleteCartItem : (String) -> Unit,
    setCheckedCartItems : () -> Unit
){
    Scaffold(
        topBar = {
            BackTopAppBar(titleResId = R.string.cart,onNavigateToBack=onNavigateToBack)
        },
        bottomBar = {
            if (uiState.carts.isNotEmpty()) {
                HorizontalDivider()
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = stringResource(id = R.string.total),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.W400
                        )
                        Text(
                            text = currency(totalCheckedPrice),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.W600
                        )
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        horizontalAlignment = Alignment.End
                    ) {
                        Button(
                            modifier = Modifier,
                            onClick = { setCheckedCartItems() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary,
                            ),
                            enabled = isButtonVisible
                        ) {
                            Text(
                                text = stringResource(id = R.string.buy),
                                fontWeight = FontWeight.W500
                            )
                        }
                    }
                }
            }
        }
    ) {
        CartContent(
            modifier = Modifier.padding(it),
            isButtonVisible = isButtonVisible,
            isAllChecked = isAllChecked,
            isLoading = uiState.isLoading,
            cart = uiState.carts,
            selectAll = selectAll,
            clearSelection= clearSelection,
            toggleItemCheck = toggleItemCheck,
            increaseQuantity = increaseQuantity,
            decreaseQuantity = decreaseQuantity,
            deleteCheckedItem = deleteCheckedItem,
            deleteCartItem = deleteCartItem
        )
    }
}

@Composable
fun CartContent(
    modifier : Modifier,
    isAllChecked : Boolean,
    isLoading: Boolean,
    isButtonVisible: Boolean,
    cart : List<Cart>,
    loadingContent: @Composable () -> Unit = {
        LoaderScreen(modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background))
    },
    selectAll : () -> Unit,
    clearSelection : () -> Unit,
    toggleItemCheck : (id: String) -> Unit,
    increaseQuantity : (String) -> Unit,
    decreaseQuantity : (String) -> Unit,
    deleteCheckedItem : () -> Unit,
    deleteCartItem : (String) -> Unit
){
    if(isLoading){
        loadingContent()
    }else if (cart.isEmpty()) {
        ErrorPage(
            title = stringResource(id = R.string.empty),
            message = stringResource(id = R.string.resource),
            button = stringResource(R.string.refresh),
            onButtonClick = {},
            alpha = 0f
        )
    } else {
        Column(modifier = modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.background)) {
            Row(modifier = Modifier.padding(start = 5.dp, end = 8.dp)) {
                Row(modifier = Modifier.fillMaxWidth().weight(1f),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isAllChecked,
                        onCheckedChange = {checked ->
                            if (checked) selectAll() else clearSelection()},
                        colors = CheckboxDefaults.colors(
                            checkedColor = MaterialTheme.colorScheme.primary,
                            uncheckedColor = MaterialTheme.colorScheme.onBackground,
                            checkmarkColor = MaterialTheme.colorScheme.background
                        )
                    )
                    Text(text = stringResource(id = R.string.choose_all))
                }

                Column(horizontalAlignment = Alignment.End) {
                    TextButton(
                        modifier = Modifier.alpha(if (isButtonVisible) 1f else 0f),
                        onClick = { deleteCheckedItem() },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.primary
                        ),
                    ) {
                        Text(
                            text = stringResource(id = R.string.erase),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W500,
                        )
                    }
                }
            }
            HorizontalDivider()
            LazyColumn(modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(cart) { item ->
                    CartListCard(
                        cart = item,
                        onChecked = {toggleItemCheck(item.productId)},
                        onDeleteCart = {deleteCartItem(item.productId)},
                        decreaseQuantity = {decreaseQuantity(item.productId)},
                        increaseQuantity = {increaseQuantity(item.productId)}
                    )
                }
            }
        }
    }
}

@Preview("Light Mode", device = Devices.PIXEL_3)
@Preview("Dark Mode", device = Devices.PIXEL_3, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CartScreenPreview(){
    EcommerceAppTheme {
        CartScreen(
            onNavigateToBack = {},
            isButtonVisible = true,
            uiState = CartUiState(
                carts = listOf(
                    Cart(
                        productId = "1",
                        productName = "ASUS ROG Strix G17 G713RM-R736H6G-O - Eclipse Gray",
                        unitPrice = 24499000,
                        productImage = "",
                        stock = 12,
                        quantity = 1,
                        isCheck = false,
                        variantName = "RAM 16GB"
                    ),
                    Cart(
                        productId = "1",
                        productName = "ASUS ROG Strix G17 G713RM-R736H6G-O - Eclipse Gray",
                        unitPrice = 24499000,
                        productImage = "",
                        stock = 8,
                        quantity = 1,
                        isCheck = false,
                        variantName = "RAM 16GB"
                    ),
                ),
                isLoading = false,
            ),
            isAllChecked = false,
            selectAll = {},
            clearSelection = {},
            toggleItemCheck = {},
            increaseQuantity = {},
            decreaseQuantity = {},
            deleteCartItem = {},
            deleteCheckedItem = {},
            setCheckedCartItems = {},
            totalCheckedPrice = 0
        )
    }
}