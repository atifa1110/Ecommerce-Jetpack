package com.example.ecommerceapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.absoluteValue

@Composable
fun DotIndicatorWithScaling(
    pagerState: PagerState,
    totalDots: Int,
    modifier: Modifier = Modifier,
    baseSize: Dp = 8.dp,
    selectedScale: Float = 1.6f,
    spacing: Dp = 8.dp,
    selectedColor: Color = MaterialTheme.colorScheme.primary,
    unselectedColor: Color = Color.LightGray) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(spacing),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(totalDots) { index ->
            val currentPage = pagerState.currentPage
            val offset = pagerState.currentPageOffsetFraction

            // Calculate scale
            val scale = when (index) {
                currentPage -> 1f + (selectedScale - 1f) * (1 - offset.absoluteValue)
                currentPage + 1 -> 1f + (selectedScale - 1f) * offset.absoluteValue
                currentPage - 1 -> 1f + (selectedScale - 1f) * offset.absoluteValue
                else -> 1f
            }

            Box(
                modifier = Modifier
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                    }
                    .size(baseSize)
                    .clip(CircleShape)
                    .background(if (index == currentPage) selectedColor else unselectedColor)
            )
        }
    }
}
