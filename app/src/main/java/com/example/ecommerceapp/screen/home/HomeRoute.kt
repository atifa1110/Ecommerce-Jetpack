package com.example.ecommerceapp.screen.home

import android.app.Activity
import android.app.LocaleManager
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.ecommerceapp.R
import com.example.ecommerceapp.ui.theme.EcommerceAppTheme

@Composable
fun HomeRoute(
    isDarkMode: Boolean,
    onNavigateToLogin: () -> Unit,
    onToggleTheme : (Boolean) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val languageCode by viewModel.languageCode.collectAsState()

    HomeScreen(
        isDarkMode = isDarkMode,
        languageCode = languageCode,
        setLanguage = viewModel::setLanguage,
        onNavigateToLogin = {
            viewModel.onLogoutClick()
            onNavigateToLogin()
        },
        onToggleTheme = onToggleTheme,

    )
}

@Composable
fun HomeScreen(
    isDarkMode: Boolean,
    languageCode : String,
    setLanguage : (String) -> Unit,
    onNavigateToLogin : () -> Unit,
    onToggleTheme : (Boolean) -> Unit,
){
    Column(modifier = Modifier.fillMaxSize()
        .background(MaterialTheme.colorScheme.background)
        .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val animationLottie by rememberLottieComposition(
            spec = LottieCompositionSpec.RawRes(resId = R.raw.animation)
        )
        val animationAction by animateLottieCompositionAsState(
            composition = animationLottie,
            iterations = LottieConstants.IterateForever
        )
        LottieAnimation(
            modifier = Modifier
                .fillMaxWidth()
                .height(182.dp),
            composition = animationLottie,
            progress = { animationAction }
        )

        Button(
            onClick = onNavigateToLogin,
            modifier = Modifier.padding(vertical = 16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text(
                text = stringResource(id = R.string.logout),
                style = MaterialTheme.typography.labelLarge
            )
        }

        val context = LocalContext.current
        val isChecked = remember(languageCode) { mutableStateOf(languageCode == "in") }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = R.string.en),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Switch(
                modifier = Modifier.padding(horizontal = 10.dp),
                checked = isChecked.value,
                onCheckedChange = { isCheckedNew ->
                    isChecked.value = isCheckedNew
                    val newCode = if (isCheckedNew) "in" else "en"

                    // 🔗 Call the unified function that handles system locale + preference saving
                    setLanguage(context, newCode)
                    setLanguage(newCode)
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = MaterialTheme.colorScheme.primary,
                    uncheckedThumbColor = MaterialTheme.colorScheme.primary,
                    uncheckedTrackColor = Color.White,
                ),
            )
            Text(
                text = stringResource(id = R.string.id),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = R.string.light),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Switch(
                modifier = Modifier.padding(horizontal = 10.dp),
                checked = isDarkMode,
                onCheckedChange = { isChecked ->
                    onToggleTheme(isChecked)
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = MaterialTheme.colorScheme.primary,
                    uncheckedThumbColor = MaterialTheme.colorScheme.primary,
                    uncheckedTrackColor = MaterialTheme.colorScheme.onPrimary,
                ),
            )
            Text(
                text = stringResource(id = R.string.dark),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

fun setLanguage(context: Context, languageCode: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        context.getSystemService(LocaleManager::class.java)
            .applicationLocales = LocaleList.forLanguageTags(languageCode)
    } else {
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(languageCode))
        (context as? Activity)?.recreate()
    }
}

@Preview("Light Mode", device = Devices.PIXEL_3)
@Preview("Dark Mode", device = Devices.PIXEL_3, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HomePreview(){
    EcommerceAppTheme {
        HomeScreen(
            isDarkMode = false,
            languageCode = "in",
            setLanguage = {},
            onNavigateToLogin = {},
            onToggleTheme = {}
        )
    }
}