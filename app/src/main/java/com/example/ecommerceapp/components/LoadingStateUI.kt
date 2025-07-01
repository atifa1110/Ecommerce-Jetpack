package com.example.ecommerceapp.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ecommerceapp.screen.main.NavigationType

@Composable
fun LoadingStateUI(
    isGrid: Boolean,
    navigationType: NavigationType
) {
    val gridCells = when (navigationType) {
        NavigationType.NAV_RAIL -> GridCells.Adaptive(minSize = 180.dp)
        NavigationType.BOTTOM_NAV -> GridCells.Fixed(2)
    }
    AnimatedFilter()
    if (isGrid) {
        LazyVerticalGrid(columns = gridCells) {
            items(10) { AnimatedGridShimmer() }
        }
    } else {
        repeat(7) {
            AnimatedListShimmer()
        }
    }
}

@Composable
fun LoadingIndicator() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
    }
}
