package com.example.ecommerceapp.firebase

import android.util.Log
import com.example.core.domain.model.CartModel
import com.example.core.ui.model.Wishlist
import com.google.firebase.analytics.FirebaseAnalytics
import javax.inject.Inject

class WishlistAnalytics @Inject constructor(
    private val analyticsManager: AnalyticsManager
) {
    fun trackAddCartButtonClicked() {
        analyticsManager.logButtonClick("Add_Cart_Button")
        Log.d("WishlistAnalytics", "Logged button_click: Logout_Button")
    }

    fun trackDeleteWishlistButtonClicked(id: String) {
        analyticsManager.logEvent("Delete_Wishlist_Button",mapOf(
            "button_id" to id
        ))
        Log.d("WishlistAnalytics", "Logged button_click: Logout_Button")
    }

    fun trackAddToCart(cart : CartModel, quantity: Int) {
        analyticsManager.logEvent(FirebaseAnalytics.Event.ADD_TO_CART, mapOf(
            FirebaseAnalytics.Param.ITEM_ID to cart.productId,
            FirebaseAnalytics.Param.ITEM_NAME to cart.productName,
            FirebaseAnalytics.Param.PRICE to cart.unitPrice.toDouble(),
            FirebaseAnalytics.Param.ITEM_BRAND to cart.variantName,
            FirebaseAnalytics.Param.QUANTITY to quantity,
        ))
        Log.d("WishlistAnalytics", "ADD_TO_CART - ${cart.productName}, qty: $quantity")
    }

    fun trackViewWishlist(wishlist: List<Wishlist>){
        val totalItems = wishlist.size
        val productIds = wishlist.joinToString(",") { it.productId }.take(40)
        val productNames = wishlist.joinToString(",") { it.productName }.take(40)

        val params = mapOf(
            "total_wishlist_items" to totalItems,
            "wishlist_item_ids" to productIds,
            "wishlist_item_names" to productNames
        )

        analyticsManager.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, params)
    }

    fun trackWishlistFailed( errorMessage: String) {
        analyticsManager.logEvent("wishlist_failed", mapOf("error_message" to errorMessage))
        Log.d("ProductAnalytics","Analytics: WISHLIST_FAILED -  Error: $errorMessage")
    }

    fun trackGridView(grid: Boolean){
        val viewType = if (grid) "grid" else "list"
        analyticsManager.logEvent("view_type_changed", mapOf(
            "view_type" to viewType
        ))
    }
}