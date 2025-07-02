package com.example.ecommerceapp.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.data.network.response.PagingError
import com.example.ecommerceapp.R
import com.example.ecommerceapp.ui.theme.poppins

data class ErrorUI(
    val title: String,
    val message: String,
    val buttonRes: String,
    val onClick: () -> Unit
)

object HttpStatus {
    const val UNAUTHORIZED = 401
    const val NOT_FOUND = 404
    const val INTERNAL_SERVER_ERROR = 500
}

@Composable
fun ErrorStateUI(
    error: Throwable,
    onResetQuery: () -> Unit,
    onRefreshAnalytics: () -> Unit,
    onRefreshToken: () -> Unit
) {
    val (title, message, buttonRes,onClick) = when (error) {
        is PagingError.ApiError -> when (error.code) {
            HttpStatus.NOT_FOUND -> ErrorUI(
                title = stringResource(id = R.string.empty),
                message = stringResource(id = R.string.resource),
                buttonRes = stringResource(id = R.string.reset),
                onClick = {
                    onResetQuery()
                    onRefreshAnalytics()
                }
            )
            HttpStatus.INTERNAL_SERVER_ERROR -> ErrorUI(
                title = error.code.toString(),
                message = stringResource(id = R.string.internal),
                buttonRes = stringResource(id = R.string.refresh),
                onClick = { onResetQuery(); onRefreshAnalytics() }
            )
            HttpStatus.UNAUTHORIZED -> ErrorUI(
                title = stringResource(id = R.string.connection),
                message = stringResource(id = R.string.connection_unavailable),
                buttonRes = stringResource(id = R.string.refresh),
                onClick = {
                    onRefreshToken()
                }
            )
            else -> return
        }

        is PagingError.ConnectionError -> ErrorUI(
            title = stringResource(id = R.string.connection),
            message = stringResource(id = R.string.connection_unavailable),
            buttonRes = stringResource(id = R.string.refresh),
            onClick = { onRefreshToken() }
        )

        is PagingError.NotFoundError -> ErrorUI(
            title = stringResource(id = R.string.empty),
            message = stringResource(id = R.string.resource),
            buttonRes = stringResource(id = R.string.reset) ,
            onClick = { onResetQuery(); onRefreshAnalytics() }
        )

        else -> return
    }

    ErrorPage(
        title = title,
        message = message,
        button = buttonRes,
        onClick = onClick,
        alpha = 1f
    )
}

@Composable
fun ErrorPage(
    title: String,
    message: String,
    button: String,
    onClick: () -> Unit,
    alpha: Float
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.size(128.dp),
            painter = painterResource(id = R.drawable.smartphone),
            contentDescription = ""
        )
        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = title,
            fontSize = 32.sp,
            fontWeight = FontWeight.W500,
            fontFamily = poppins
        )
        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = message,
            style = MaterialTheme.typography.bodyLarge
        )
        Button(
            modifier = Modifier
                .padding(top = 8.dp)
                .alpha(alpha),
            onClick = { onClick() },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text(
                text = button,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}