package com.example.ecommerceapp.screen.review

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ecommerceapp.R
import com.example.ecommerceapp.components.BackTopAppBar
import com.example.ecommerceapp.components.ErrorPage
import com.example.ecommerceapp.components.LoaderScreen
import com.example.ecommerceapp.components.ReviewListCard
import com.example.ecommerceapp.data.ui.Review

@Composable
fun ReviewRoute(
    viewModel: ReviewViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getReviewProduct()
    }
    ReviewScreen(uiState = uiState)
}

@Composable
fun ReviewScreen(
    uiState: ReviewUiState
) {
    Scaffold(
        topBar = {
            BackTopAppBar(titleResId = R.string.review_buyer) { }
        },
    ) {
        ReviewContent(
            modifier = Modifier.padding(it),
            isLoading = uiState.isLoading,
            isError = uiState.isError,
            reviews = uiState.reviews
        )
    }
}

@Composable
fun ReviewContent(
    modifier : Modifier,
    isLoading : Boolean,
    isError: Boolean,
    reviews : List<Review>,
    loadingContent: @Composable () -> Unit = {
        LoaderScreen(modifier = Modifier.fillMaxSize())
    },
) {
    Column(modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        when {
            isLoading -> {
                loadingContent()
            }

            isError -> {
                ErrorPage(
                    title = stringResource(id = R.string.empty),
                    message = stringResource(id = R.string.resource),
                    button = stringResource(R.string.refresh),
                    onButtonClick = {},
                    1F
                )
            }

            else -> {

                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(reviews) { review ->
                            ReviewListCard(
                                review =  review
                            )
                        }
                    }
            }
        }
    }
}