package com.example.ecommerceapp.screen.boarding

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import com.example.ecommerceapp.R
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState

@Composable
fun OnBoardingRoute(
    onNavigateToLogin: () -> Unit,
    onNavigateToRegister: () -> Unit,
    viewModel: OnBoardingViewModel = hiltViewModel()
){
    OnBoardingScreen(
        onNavigateToLogin = {
            onNavigateToLogin()
            viewModel.setBoardingState(true)
        },
        onNavigateToRegister = {
            onNavigateToRegister()
            viewModel.setBoardingState(true)
        }
    )
}

@Composable
fun OnBoardingScreen (
    onNavigateToLogin: () -> Unit,
    onNavigateToRegister: () -> Unit
){
    val pages = listOf(OnBoardingPage.First, OnBoardingPage.Second, OnBoardingPage.Third)
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        HorizontalPager(
            modifier = Modifier.weight(1f),
            count = 3,
            state = pagerState,
            verticalAlignment = Alignment.CenterVertically
        ) { position ->
            PagerScreen(onBoardingPage = pages[position])
        }

        Button(onClick = onNavigateToRegister, modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            enabled = true
        ) {
            Text(
                text = stringResource(id = R.string.join),
                fontWeight = FontWeight.W500
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // column button skip
            Column(
                Modifier.weight(1f),
                horizontalAlignment = Alignment.Start
            ) {
                TextButton(
                    onClick = onNavigateToLogin,
                    modifier = Modifier,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        text = stringResource(id = R.string.skip),
                        fontSize = 14.sp,
                    )
                }
            }
            // colum pager indicator
            Column(
                Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HorizontalPagerIndicator(modifier = Modifier.padding(5.dp), pagerState = pagerState)
            }
            // column button next
            Column(
                Modifier.weight(1f),
                horizontalAlignment = Alignment.End
            ) {
                TextButton(
                    onClick = {
                        if (pagerState.currentPage + 1 < pages.size) {
                            scope.launch {
                                pagerState.scrollToPage(pagerState.currentPage + 1)
                            }
                        }
                    },
                    modifier = Modifier.alpha(if (pagerState.currentPage == 2) 0f else 1f),
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary // teks button
                    )
                ) {
                    Text(
                        text = stringResource(id = R.string.next),
                        fontSize = 14.sp,
                    )
                }
            }
        }
    }
}

@Composable
fun PagerScreen(onBoardingPage: OnBoardingPage) {
    Column {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = onBoardingPage.image),
            contentDescription = "Pager Image"
        )
    }
}

@Preview("Light Mode", device = Devices.PIXEL_3)
@Preview("Dark Mode", device = Devices.PIXEL_3, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun OnBoardingPreview() {
    OnBoardingScreen (
        onNavigateToLogin = {},
        onNavigateToRegister = { }
    )
}
