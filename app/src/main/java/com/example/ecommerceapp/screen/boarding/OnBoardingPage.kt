package com.example.ecommerceapp.screen.boarding

import androidx.annotation.DrawableRes
import com.example.ecommerceapp.R

sealed class OnBoardingPage(
    @DrawableRes
    val image: Int,
) {
    data object First : OnBoardingPage(
        image = R.drawable.page1,
    )

    data object Second : OnBoardingPage(
        image = R.drawable.page2,
    )

    data object Third : OnBoardingPage(
        image = R.drawable.page3,
    )
}
