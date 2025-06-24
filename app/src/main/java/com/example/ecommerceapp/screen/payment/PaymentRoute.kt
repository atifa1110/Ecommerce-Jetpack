package com.example.ecommerceapp.screen.payment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ecommerceapp.R
import com.example.ecommerceapp.components.BackTopAppBar
import com.example.ecommerceapp.components.LoaderScreen
import com.example.ecommerceapp.components.PaymentListCart
import com.example.core.ui.model.Payment
import kotlinx.coroutines.flow.collectLatest

@Composable
fun PaymentRoute(
    onNavigateToBack: () -> Unit,
    onItemClick: (payment: Payment.PaymentItem) -> Unit,
    viewModel: PaymentViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is PaymentEvent.ShowSnackbar -> {
                    snackBarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    PaymentScreen(
        onNavigateBack = onNavigateToBack,
        onItemClick = { payment ->
            viewModel.paymentInfoAnalytics(payment)
            onItemClick(payment)
        },
        isLoading = uiState.isLoading,
        result = uiState.paymentItem,
        snackBarHostState = snackBarHostState
    )
}


@Composable
fun PaymentScreen(
    onNavigateBack: () -> Unit,
    onItemClick: (payment: Payment.PaymentItem) -> Unit,
    isLoading : Boolean,
    result: List<Payment>,
    snackBarHostState: SnackbarHostState
) {
    Scaffold(
        snackbarHost = {SnackbarHost(hostState = snackBarHostState)},
        topBar = {
            BackTopAppBar(titleResId = R.string.choose_payment,
                onNavigateToBack = onNavigateBack)
        }
    ) {
        PaymentContent(
            modifier = Modifier.padding(it),
            onItemClick = onItemClick,
            isLoading = isLoading,
            result = result
        )
    }
}

@Composable
fun PaymentContent(
    modifier: Modifier,
    onItemClick: (payment: Payment.PaymentItem) -> Unit,
    isLoading : Boolean,
    result: List<Payment>,
    loadingContent: @Composable () -> Unit = {
        LoaderScreen(modifier = Modifier.fillMaxSize().background(Color.Transparent))
    },
){
    Column(modifier = modifier.background(MaterialTheme.colorScheme.background)) {
        if (isLoading) {
            loadingContent()
        }

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            if (result.isNotEmpty()) {
                items(result.size) { index ->
                    PaymentComposable(result[index], onItemClick)
                }
            }
        }
    }
}

@Composable
fun PaymentComposable(
    payment: Payment,
    onItemClick: (payment: Payment.PaymentItem) -> Unit
) {
    Column(modifier = Modifier) {
        Column(modifier = Modifier.fillMaxWidth()
            .padding(start = 16.dp, top = 16.dp)
        ) {
            Text(
                text = payment.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.W500
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Column {
            payment.item.forEach { virtual ->
                Column {
                    PaymentListCart (virtual, onItemClick)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(thickness = 4.dp)
    }
}
