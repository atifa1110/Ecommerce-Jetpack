package com.example.ecommerceapp.screen.checkout

import android.content.res.Configuration
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AddCard
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.ecommerceapp.R
import com.example.ecommerceapp.components.BackTopAppBar
import com.example.ecommerceapp.components.CheckoutListCart
import com.example.ecommerceapp.components.LoaderScreen
import com.example.core.ui.model.Cart
import com.example.core.ui.model.Payment
import com.example.ecommerceapp.screen.shared.SharedViewModel
import com.example.ecommerceapp.ui.theme.EcommerceAppTheme
import com.example.ecommerceapp.utils.currency
import kotlinx.coroutines.flow.collectLatest

@Composable
fun CheckoutRoute(
    onNavigateToBack: () -> Unit,
    onNavigateToStatus : () -> Unit,
    onNavigateToPayment : () -> Unit,
    payment : Payment.PaymentItem,
    viewModel: CheckoutViewModel = hiltViewModel(),
) {
    val sharedViewModel: SharedViewModel = hiltViewModel(LocalContext.current as ComponentActivity)
    val uiState by viewModel.uiState.collectAsState()
    val state by sharedViewModel.uiState.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.fulfillmentState.isSuccess) {
        if (uiState.fulfillmentState.isSuccess) {
            sharedViewModel.setFulfillment(uiState.fulfillmentState.fulfillment)
            onNavigateToStatus()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is CheckoutEvent.ShowSnackbar -> {
                    snackBarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    CheckoutScreen(
        uiState = uiState,
        carts = state.checkedCarts,
        payment = payment,
        totalPrice = viewModel.calculateTotalPrice(state.checkedCarts),
        snackBarHostState = snackBarHostState,
        onNavigateToBack = onNavigateToBack,
        onNavigateToPayment = {
            viewModel.paymentButtonClick()
            onNavigateToPayment()
        },
        onPaymentTransaction = {
            viewModel.fulfillmentTransaction(payment,state.checkedCarts)
        },
        increaseQuantity = {sharedViewModel.updateQuantity(it,true)},
        decreaseQuantity = {sharedViewModel.updateQuantity(it,false)}
    )
}

@Composable
fun CheckoutScreen(
    uiState: CheckoutUiState,
    carts : List<Cart>,
    payment : Payment.PaymentItem,
    totalPrice : Int,
    snackBarHostState: SnackbarHostState,
    loadingContent: @Composable () -> Unit = {
        LoaderScreen(modifier = Modifier.fillMaxSize())
    },
    onNavigateToBack: () -> Unit,
    onNavigateToPayment : () -> Unit,
    onPaymentTransaction :  () -> Unit,
    increaseQuantity: (String) -> Unit,
    decreaseQuantity: (String) -> Unit,
){
    if(uiState.fulfillmentState.isLoading) {
        loadingContent()
    }else{
        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
            topBar = {
                BackTopAppBar(titleResId = R.string.checkout, onNavigateToBack = onNavigateToBack)
            },
            bottomBar = {
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
                            style = MaterialTheme.typography.bodySmall
                        )

                        Text(
                            text = currency(totalPrice),
                            style = MaterialTheme.typography.titleSmall
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
                            onClick = {
                                onPaymentTransaction()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            enabled = payment.label.isNotEmpty()
                        ) {
                            Text(
                                text = stringResource(id = R.string.pay),
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }
                }
            }
        ) {
            CheckoutContent(
                modifier = Modifier.padding(it),
                paymentItem = payment,
                checkoutItem = carts,
                onNavigateToPayment = onNavigateToPayment,
                decreaseQuantity = decreaseQuantity,
                increaseQuantity = increaseQuantity
            )
        }
    }
}

@Composable
fun CheckoutContent(
    modifier: Modifier,
    paymentItem: Payment.PaymentItem,
    checkoutItem: List<Cart>,
    onNavigateToPayment : () -> Unit,
    increaseQuantity: (String) -> Unit,
    decreaseQuantity: (String) -> Unit,
) {
    LazyColumn(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = stringResource(id = R.string.purchased_items),
                style = MaterialTheme.typography.titleMedium
            )
        }

        items(checkoutItem) { item ->
            CheckoutListCart(
                cart = item,
                decreaseQuantity = { decreaseQuantity(item.productId) },
                increaseQuantity = { increaseQuantity(item.productId) }
            )
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(thickness = 2.dp)
        }

        item {
            Text(
                text = stringResource(id = R.string.payment),
                fontSize = 16.sp,
                fontWeight = FontWeight.W500
            )

            Spacer(modifier = Modifier.height(10.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToPayment() },
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(3.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onBackground
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        PaymentImageBox(paymentItem.image)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = if (paymentItem.label.isEmpty())
                                stringResource(id = R.string.choose_payment)
                            else paymentItem.label,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W500,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Arrow",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }
}


@Composable
fun PaymentImageBox(imageUrl: String) {
    val painter = rememberAsyncImagePainter(
        model = imageUrl,
        placeholder = painterResource(R.drawable.add_card),
        error = painterResource(R.drawable.add_card)
    )
    val state = painter.state

    Box(
        modifier = Modifier.size(
            width = if (state is AsyncImagePainter.State.Success) Dp.Unspecified else 24.dp,
            height = 24.dp
        )
    ) {
        when {
            imageUrl.isEmpty() || state is AsyncImagePainter.State.Error -> {
                Icon(
                    imageVector = Icons.Default.AddCard,
                    contentDescription = "Fallback image",
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            else -> {
                Image(
                    painter = painter,
                    contentDescription = "Payment image",
                    modifier = Modifier.size(50.dp),
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}


@Preview("Light Mode", device = Devices.PIXEL_3)
@Preview("Dark Mode", device = Devices.PIXEL_3, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CheckoutPreview() {
    EcommerceAppTheme {
        val snackBarHostState = remember { SnackbarHostState() }
        CheckoutScreen(
            carts = emptyList(),
            uiState = CheckoutUiState(),
            totalPrice = 25000000,
            payment = Payment.PaymentItem(
                label = "Bank BCA",
                image = "https://upload.wikimedia.org/wikipedia/commons/thumb/5/5c/Bank_Central_Asia.svg/2560px-Bank_Central_Asia.svg.png",
                status = false
            ),
            snackBarHostState = snackBarHostState,
            onPaymentTransaction = {},
            onNavigateToBack = {},
            onNavigateToPayment = {},
            increaseQuantity = {},
            decreaseQuantity = {}
        )
    }
}