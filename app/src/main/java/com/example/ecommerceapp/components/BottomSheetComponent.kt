package com.example.ecommerceapp.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecommerceapp.R
import com.example.ecommerceapp.ui.theme.EcommerceAppTheme
import com.example.ecommerceapp.ui.theme.poppins


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun BottomSheetFilter(
    selectedSort: String,
    selectedBrand: String,
    lowestPrice: String,
    highestPrice: String,
    categoryItems : Array<String>,
    sortItems : Array<String>,
    isBottomSheetOpen : Boolean,
    setBottomSheetOpen : (Boolean) -> Unit,
    updateSelectedSort : (String) -> Unit,
    updateSelectedBrand : (String) -> Unit,
    updatePriceLowest : (String) -> Unit,
    updatePriceHighest : (String) -> Unit,
    onResetQuery : () -> Unit,
    onSetQuery: (brand: String?, low: String?, high: String?, sort: String?) -> Unit,
){
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    if (isBottomSheetOpen) {

        ModalBottomSheet(
            onDismissRequest = { setBottomSheetOpen(false) },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.background
        ) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 24.dp)
            ) {
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        Modifier.weight(1f),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = stringResource(id = R.string.filter),
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                    Column(
                        Modifier.weight(1f),
                        horizontalAlignment = Alignment.End
                    ) {
                        TextButton(onClick = {
                            onResetQuery()
                            setBottomSheetOpen(false)
                        }) {
                            Text(
                                text = stringResource(id = R.string.reset),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                Text(
                    text = stringResource(id = R.string.sort),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W600,
                    fontFamily = poppins
                )
                FlowRow(modifier = Modifier.fillMaxWidth()) {
                    sortItems.forEach { item ->
                        FilterChip(
                            modifier = Modifier.padding(end = 6.dp),
                            selected = (selectedSort == item),
                            onClick = { updateSelectedSort(item) },
                            label = {
                                Text(text = item,
                                    style= MaterialTheme.typography.bodyMedium)
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    stringResource(id = R.string.category),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W600,
                    fontFamily = poppins
                )
                LazyRow(modifier = Modifier.fillMaxWidth()) {
                    items(categoryItems) { item ->
                        FilterChip(
                            modifier = Modifier.padding(end = 6.dp),
                            selected = (selectedBrand == item),
                            onClick = { updateSelectedBrand(item)},
                            label = {
                                Text(text = item)
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    stringResource(id = R.string.price),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W600,
                    fontFamily = poppins
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    PriceComponent(
                        label = R.string.lowest,
                        price = lowestPrice,
                        onPriceChange = {updatePriceLowest(it)},
                        modifier = Modifier.weight(1f)
                    )
                    PriceComponent(
                        label = R.string.highest,
                        price = highestPrice,
                        onPriceChange = {updatePriceHighest(it)},
                        modifier = Modifier.weight(1f)
                    )
                }

                ButtonComponent(
                    onClick = {
                        onSetQuery(selectedBrand,lowestPrice,highestPrice,selectedSort)
                        setBottomSheetOpen(false)
                    },
                    enable = true,
                    buttonText = R.string.apply
                )
            }
        }
    }
}

@Preview("Light Mode", device = Devices.PIXEL_3)
@Preview("Dark Mode", device = Devices.PIXEL_3, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun BottomSheetPreview() {
    EcommerceAppTheme {
        BottomSheetFilter(
            selectedSort = "sort",
            selectedBrand = "brand",
            lowestPrice = "0",
            highestPrice = "100",
            categoryItems = arrayOf("brand", "brand1", "brand4"),
            sortItems = arrayOf("sort" ,"sort1", "sort2"),
            isBottomSheetOpen = false,
            setBottomSheetOpen = {},
            updatePriceLowest = {},
            updatePriceHighest = {},
            updateSelectedSort = {},
            updateSelectedBrand = {},
            onResetQuery = {},
            onSetQuery = {_,_,_,_->}
        )
    }
}
