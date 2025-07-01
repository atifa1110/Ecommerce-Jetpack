package com.example.ecommerceapp.screen.store

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ecommerceapp.R
import com.example.ecommerceapp.components.SearchCard
import com.example.ecommerceapp.ui.theme.EcommerceAppTheme
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun SearchScreen(
    uiState: SearchSuggestionState,
    search: String,
    onSearchChange: (String) -> Unit,
    searchProduct: () -> Unit,
    setSearchScreenOpen: (Boolean) -> Unit,
    errorMessage: String?,
    onSuggestionSelected: (String) -> Unit
) {
    val visible by remember { mutableStateOf(true) }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(animationSpec = tween(durationMillis = 1000)),
        exit = slideOutVertically(animationSpec = tween(durationMillis = 1000))
    ) {
        Column(modifier = Modifier.fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth().focusRequester(focusRequester)
                    .onFocusChanged {
                        if (it.isFocused) {
                            keyboardController?.show()
                        }
                    },
                placeholder = {
                    Text(
                        text = stringResource(R.string.search),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                },
                textStyle = MaterialTheme.typography.bodyMedium,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        searchProduct()
                    }
                ),
                singleLine = true,
                maxLines = 1,
                value = search,
                onValueChange = { onSearchChange(it) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )
                },
                trailingIcon = {
                    Icon(
                        modifier = Modifier.clickable {
                            if(search.isEmpty()){
                                setSearchScreenOpen(false)
                            }else {
                                onSearchChange("")
                            }
                        },
                        imageVector = Icons.Outlined.Cancel,
                        contentDescription = "close"
                    )
                }
            )

            when {
                uiState.isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                errorMessage == null && search.isNotBlank() && uiState.isSuccess && uiState.suggestions.isEmpty() -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Search is Empty",
                            style = MaterialTheme.typography.bodyMedium)
                    }
                }

                errorMessage != null -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = errorMessage,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }

                else -> {
                    LazyColumn {
                        itemsIndexed(uiState.suggestions) { index, item ->
                            key(index) {
                                SearchCard(
                                    productName= item,
                                    modifier = Modifier.clickable {
                                        onSuggestionSelected(item)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview("Light Mode", device = Devices.PIXEL_3)
@Preview("Dark Mode", device = Devices.PIXEL_3, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun LoginPreview() {
    EcommerceAppTheme {
        SearchScreen(
            uiState = SearchSuggestionState(
                suggestions = listOf(
                    "Lenovo Legion 3",
                    "Lenovo Legion 5",
                    "Lenovo Legion 7",
                    "Lenovo Ideapad 3",
                    "Lenovo Ideapad 5",
                    "Lenovo Ideapad 7"
                ),
                isLoading = false,
            ),
            search = "Lenovo",
            onSearchChange = {},
            setSearchScreenOpen = {},
            onSuggestionSelected = {},
            searchProduct = {},
            errorMessage = ""
        )
    }
}
