package com.example.ecommerceapp.screen.transaction

import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ecommerceapp.components.ErrorPage
import com.example.ecommerceapp.components.LoaderScreen
import com.example.ecommerceapp.components.TransactionListCard
import com.example.ecommerceapp.data.ui.Transaction
import com.example.ecommerceapp.R
import com.example.ecommerceapp.data.ui.ItemTransaction
import com.example.ecommerceapp.screen.shared.SharedViewModel
import com.example.ecommerceapp.ui.theme.EcommerceAppTheme

@Composable
fun TransactionRoute(
    onNavigateToStatus : () -> Unit,
    viewModel: TransactionViewModel = hiltViewModel(),
) {
    val sharedViewModel: SharedViewModel = hiltViewModel(LocalContext.current as ComponentActivity)
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getTransaction()
    }

    TransactionScreen(
        isLoading = uiState.isLoading,
        isError = uiState.isError,
        transactions = uiState.transactions,
        onNavigateToStatus = {
            sharedViewModel.setTransaction(it)
            onNavigateToStatus()
        }
    )
}

@Composable
fun TransactionScreen(
    isLoading : Boolean,
    isError: Boolean,
    transactions : List<Transaction>,
    loadingContent: @Composable () -> Unit = {
        LoaderScreen(modifier = Modifier.fillMaxSize())
    },
    onNavigateToStatus : (Transaction) -> Unit,
){
    Column(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
        when {
            isLoading -> {
                loadingContent()
            }

            isError -> {
                ErrorPage(
                    title = stringResource(id = R.string.empty),
                    message = stringResource(id = R.string.resource),
                    button = stringResource(R.string.refresh),
                    onButtonClick = { },
                    alpha = 1F
                )
            }

            transactions.isEmpty() -> {
                ErrorPage(
                    title = stringResource(id = R.string.empty),
                    message = stringResource(id = R.string.resource),
                    button = stringResource(R.string.refresh),
                    onButtonClick = { },
                    alpha = 0F
                )
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 16.dp, horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(transactions) { item ->
                        TransactionListCard(
                            transaction = item,
                            onNavigateToStatus = {
                                onNavigateToStatus(item)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview("Light Mode", device = Devices.PIXEL_3)
@Composable
fun TransactionScreenPreview() {
    EcommerceAppTheme {
        TransactionScreen(
            isLoading = false,
            isError = true,
            transactions = listOf(
                Transaction(
                    invoiceId = "2e77cfe0-6a5a-4a2d-9db9-9ee9ad6692b0",
                    status = true,
                    date= "04 Jun 2025",
                    time = "12:03",
                    payment = "Bank BCA",
                    total = 31848000,
                    items = listOf(
                        ItemTransaction(
                            productId ="685817b3-ae28-4e36-a409-e2b5448aeba1",
                            variantName= "RAM 16GB",
                            quantity= 1
                        )
                    ),
                    rating = 0,
                    review = "",
                    image = "https://images.tokopedia.net/img/cache/900/VqbcmM/2022/2/12/748b3fbf-d7c8-44f3-a8e6-35d00394974c.png",
                    name = "LENOVO 3"
                ),
                Transaction(
                    invoiceId = "c08c6af8-2861-47f9-a0f4-cfa3c7972b63",
                    status = true,
                    date= "07 Jun 2025",
                    time = "12:03",
                    payment = "Bank BCA",
                    total = 17999000,
                    items = listOf(
                        ItemTransaction(
                            productId ="685817b3-ae28-4e36-a409-e2b5448aeba1",
                            variantName= "RAM 16GB",
                            quantity= 5
                        )
                    ),
                    rating = 4,
                    review = "Very Good",
                    image = "https://images.tokopedia.net/img/cache/900/VqbcmM/2022/2/12/748b3fbf-d7c8-44f3-a8e6-35d00394974c.png",
                    name = "DELL ALIENWARE M15 R5 RYZEN 7 5800 16GB 512SSD RTX3050Ti 4GB W10 15.6F - Hitam, UNIT"
                ),
            ),
            onNavigateToStatus = {}
        )
    }
}