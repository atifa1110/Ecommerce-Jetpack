package com.example.ecommerceapp.screen.notification

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ecommerceapp.R
import com.example.ecommerceapp.components.BackTopAppBar
import com.example.ecommerceapp.components.ErrorPage
import com.example.ecommerceapp.components.LoaderScreen
import com.example.ecommerceapp.components.NotificationListCard
import com.example.core.ui.model.Notification
import com.example.ecommerceapp.ui.theme.EcommerceAppTheme

@Composable
fun NotificationRoute(
    isNotification : String?,
    onNavigateToMain : () -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: NotificationViewModel = hiltViewModel()
){
    val uiState by viewModel.uiState.collectAsState()

    NotificationScreen(
        uiState = uiState,
        isNotification = isNotification,
        onNavigateToMain = onNavigateToMain,
        onNavigateBack = onNavigateBack,
        setNotificationRead = viewModel::updateNotification
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    uiState: NotificationUiState,
    isNotification : String?,
    onNavigateToMain : () -> Unit,
    onNavigateBack: () -> Unit,
    setNotificationRead: (id: Int, read: Boolean) -> Unit,
){
    Scaffold(
        topBar = {
            BackTopAppBar(
                titleResId = R.string.notification,
                onNavigateToBack= {
                    if (!isNotification.isNullOrEmpty()) onNavigateToMain() else onNavigateBack()
                }
            )
        }
    ) {
       NotificationContent(
           isLoading = uiState.isLoading,
           modifier = Modifier.padding(it),
           notificationList = uiState.notifications,
           setNotificationRead = setNotificationRead
       )
    }
}

@Composable
fun NotificationContent(
    isLoading : Boolean,
    modifier : Modifier,
    loadingContent: @Composable () -> Unit = {
        LoaderScreen(modifier = Modifier.fillMaxSize())
    },
    notificationList: List<Notification>,
    setNotificationRead: (id: Int, read: Boolean) -> Unit,
){
    Column(modifier = modifier.background(MaterialTheme.colorScheme.background).fillMaxSize()) {
        if (isLoading){
          loadingContent()
        } else if (notificationList.isEmpty()) {
            ErrorPage(
                title = stringResource(id = R.string.empty),
                message = stringResource(id = R.string.resource),
                button = stringResource(R.string.refresh),
                onButtonClick = { /*TODO*/ },
                alpha = 0f
            )
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(notificationList) { notification ->
                    NotificationListCard(
                        notification = notification,
                        setNotificationRead = { id, read ->
                            setNotificationRead(id,read)
                        },
                    )
                }
            }
        }
    }
}

@Preview("Light Mode", device = Devices.PIXEL_3)
@Preview("Dark Mode", device = Devices.PIXEL_3, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun NotificationPreview(){
    EcommerceAppTheme {
        NotificationScreen(
            uiState = NotificationUiState(
                isLoading = false,
                isError = false,
                notifications = listOf(
                    Notification(
                        1,"Telkomsel Award 2023",
                        "Nikmati Kemeriahan ulang tahun Telkomsel pada har jumat 21 Juli 2023 pukul 19.00 - 21.00 WIB langsung dari Beach City International Stadium dengan berbagai kemudahan untuk mendapatkan aksesnya.",
                        "",
                        "Promo",
                        "21 Jul 2023","12:34", false
                    ),
                    Notification(
                        1,"Telkomsel Award 2023",
                        "Nikmati Kemeriahan ulang tahun Telkomsel pada har jumat 21 Juli 2023 pukul 19.00 - 21.00 WIB langsung dari Beach City International Stadium dengan berbagai kemudahan untuk mendapatkan aksesnya.",
                        "",
                        "Promo",
                        "21 Jul 2023","12:34", true
                    )
                )
            ),
            isNotification = "",
            onNavigateBack = {},
            onNavigateToMain = {},
            setNotificationRead = {_,_ ->}
        )
    }
}