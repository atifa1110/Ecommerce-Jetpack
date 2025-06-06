package com.example.ecommerceapp.screen.status

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ecommerceapp.R
import com.example.ecommerceapp.components.BackCenterTopAppBar
import com.example.ecommerceapp.components.LoaderScreen
import com.example.ecommerceapp.components.TextComponent
import com.example.ecommerceapp.data.ui.Fulfillment
import com.example.ecommerceapp.screen.shared.SharedViewModel
import com.example.ecommerceapp.ui.theme.DarkPurple
import com.example.ecommerceapp.ui.theme.EcommerceAppTheme
import com.example.ecommerceapp.utils.currency

@Composable
fun StatusRoute(
    onNavigateToTransaction : () -> Unit,
    onBackButton : () -> Unit,
    viewModel: StatusViewModel = hiltViewModel()
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val sharedViewModel: SharedViewModel = hiltViewModel(LocalContext.current as ComponentActivity)
    val sharedUiState by sharedViewModel.uiState.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isSuccess) {
        if(uiState.isSuccess) {
            onNavigateToTransaction()
        }
    }

    StatusScreen(
        isLoading = uiState.isLoading,
        rating = uiState.rating,
        review = uiState.review,
        userMessage = uiState.userMessage,
        onReviewChanged = viewModel::onReviewChanged,
        onRatingChanged = viewModel::onRatingChanged,
        onRatingTransaction = {
            viewModel.setRatingTransaction(sharedUiState.fulfillment)
        },
        onBackButton = onBackButton,
        fulfillment = sharedUiState.fulfillment,
        snackBarHostState = snackBarHostState,
        snackBarMessageShown = viewModel::clearUserMessage
    )
}

@Composable
fun StatusScreen(
    isLoading : Boolean,
    rating: Int,
    review: String,
    userMessage: String?,
    onReviewChanged : (String) -> Unit,
    onRatingChanged : (Int) -> Unit,
    onRatingTransaction : () -> Unit,
    onBackButton : () -> Unit,
    fulfillment: Fulfillment,
    loadingContent: @Composable () -> Unit = {
        LoaderScreen(modifier = Modifier.fillMaxSize())
    },
    snackBarHostState : SnackbarHostState,
    snackBarMessageShown :() -> Unit
) {
    if(isLoading){
        loadingContent()
    }else {
        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = snackBarHostState)
            },
            topBar = {
                BackCenterTopAppBar(
                    titleResId = R.string.status,
                    onNavigateToBack = onBackButton
                )
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
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { onRatingTransaction() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary,
                            )
                        ) {
                            Text(
                                text = stringResource(id = R.string.done),
                                fontWeight = FontWeight.W500
                            )
                        }
                    }
                }
            }
        ) {
            StatusContent(
                modifier = Modifier.padding(it),
                rating = rating,
                review = review,
                fulfillment = fulfillment,
                onReviewChanged = onReviewChanged,
                onRatingChanged = onRatingChanged,
            )
        }

        if (userMessage?.isNotBlank() == true) {
            LaunchedEffect(userMessage) {
                snackBarHostState.showSnackbar(userMessage)
                snackBarMessageShown() // Called after snackbar hides
            }
        }
    }
}

@Composable
fun StatusContent(
    modifier : Modifier,
    rating: Int,
    review: String,
    fulfillment: Fulfillment,
    onRatingChanged : (Int) -> Unit,
    onReviewChanged : (String) -> Unit,
){
    Column(modifier = modifier
        .background(MaterialTheme.colorScheme.background)
        .fillMaxSize()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Card(
                    modifier = Modifier.padding(top = 30.dp),
                    shape = RoundedCornerShape(8.dp),
                    elevation = CardDefaults.cardElevation(3.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(modifier = Modifier
                        .padding(top = 40.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = stringResource(id = R.string.payment_success),
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.W600
                            )
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            RatingBarStatus(
                                maxRating = 5,
                                rating = rating,
                                onRatingChanged = onRatingChanged
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(id = R.string.leave_review),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W600
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        TextComponent(text = review, onTextChanged = onReviewChanged, label = R.string.leave_review)
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Card(shape = CircleShape) {
                        Column(modifier = Modifier
                            .background(DarkPurple)
                            .padding(16.dp)
                        ) {
                            Icon(
                                tint = MaterialTheme.colorScheme.onBackground,
                                imageVector = Icons.Default.Check,
                                contentDescription = "Check"
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.detail_transaction),
                fontSize = 14.sp,
                fontWeight = FontWeight.W600
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                Text(
                    text = stringResource(id = R.string.transaction_id),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.W400
                )
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = fulfillment.invoiceId,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.W600
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row {
                Text(
                    text = stringResource(id = R.string.status),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.W400
                )
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(text = stringResource(R.string.Success), fontSize = 12.sp, fontWeight = FontWeight.W600)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row {
                Text(
                    text = stringResource(id = R.string.date),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.W400
                )
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text =fulfillment.date,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.W600
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row {
                Text(
                    text = stringResource(id = R.string.time),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.W400
                )
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = fulfillment.time,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.W600
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row {
                Text(
                    text = stringResource(id = R.string.payment_method),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.W400
                )
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = fulfillment.payment,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.W600
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row {
                Text(
                    text = stringResource(id = R.string.total_payment),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.W400
                )
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = currency(fulfillment.total),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.W600
                    )
                }
            }
        }
    }
}

@Composable
fun RatingBarStatus(
    maxRating: Int = 5,
    rating: Int,
    onRatingChanged: (Int) -> Unit, // pass selected star index
    activeColor: Color = Color(0xFFFFC107),
    inactiveColor: Color = Color.LightGray,
    modifier: Modifier = Modifier
) {
    Row {
        for (i in 1..maxRating) {
            val isFilled = i <= rating
            val iconColor = if (isFilled) activeColor else inactiveColor

            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = iconColor,
                modifier = modifier
                    .size(44.dp) // ⬅️ size per star
                    .clickable { onRatingChanged(i) } // ⬅️ each star is clickable
                    .padding(4.dp)
            )
        }
    }
}

@Composable
fun RatingBar(
    maxRating: Int = 5,
    rating: Int,
    activeColor: Color = Color(0xFFFFC107),
    inactiveColor: Color = Color.LightGray,
    modifier: Modifier = Modifier
) {
    Row {
        for (i in 1..maxRating) {
            val isFilled = i <= rating
            val iconColor = if (isFilled) activeColor else inactiveColor

            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = iconColor,
                modifier = modifier
            )
        }
    }
}

@Preview("Light Mode", device = Devices.PIXEL_3)
@Preview("Dark Mode", device = Devices.PIXEL_3, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun StatusPreview(){
    EcommerceAppTheme {
        val snackBarHostState = remember { SnackbarHostState() }
        StatusScreen(
            isLoading = false,
            rating = 4,
            review = "",
            userMessage = "",
            onReviewChanged = {},
            onRatingTransaction = {},
            onBackButton = {},
            onRatingChanged = {},
            fulfillment = Fulfillment("1",false,"13 Oct 2020","13:70","BCA",35000000),
            snackBarHostState = snackBarHostState,
            snackBarMessageShown = {}
        )
    }
}