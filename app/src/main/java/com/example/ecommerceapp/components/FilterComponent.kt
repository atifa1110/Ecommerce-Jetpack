package com.example.ecommerceapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.ecommerceapp.R

@Composable
fun FilterComponent(
    isClickedGrid : Boolean,
    filters : List<String>,
    setClickedGrid : () -> Unit,
    setBottomSheetOpen : (Boolean) -> Unit
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            AssistChip(
                onClick = { setBottomSheetOpen(true) },
                label = {
                    Text(text = stringResource(id = R.string.filter),
                        style= MaterialTheme.typography.bodyMedium)
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Tune,
                        contentDescription = null,
                        modifier = Modifier.size(AssistChipDefaults.IconSize)
                    )
                }
            )

            if (filters.isNotEmpty()) {
                Spacer(modifier = Modifier.width(8.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp)
                ) {
                    items(filters) { filter ->
                        AssistChip(
                            onClick = { /* Optional: filter remove */ },
                            label = { Text(text = filter,
                                style= MaterialTheme.typography.bodyMedium) }
                        )
                    }
                }
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Spacer(
                modifier = Modifier
                    .height(24.dp)
                    .width(1.dp)
                    .background(Color.Gray)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Icon(
                modifier = Modifier.clickable { setClickedGrid() },
                imageVector = if (isClickedGrid) {
                    Icons.Default.GridView
                } else Icons.AutoMirrored.Filled.FormatListBulleted,
                contentDescription = "List/Grid Toggle",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}