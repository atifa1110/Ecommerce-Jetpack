package com.example.ecommerceapp.firebase

import android.os.Bundle
import android.util.Log
import com.example.core.ui.model.Cart
import com.example.core.ui.model.ProductDetail
import com.example.core.ui.model.ProductVariant
import com.google.firebase.analytics.FirebaseAnalytics
import javax.inject.Inject

class CartAnalytics @Inject constructor(
    private val analyticsManager: AnalyticsManager
) {
    fun trackRemoveFromCart(productId: String) {
        analyticsManager.logEvent(
            FirebaseAnalytics.Event.REMOVE_FROM_CART,
            mapOf(FirebaseAnalytics.Param.ITEM_ID to productId)
        )
        Log.d("CartAnalytics", "REMOVE_FROM_CART - ID: $productId")
    }

    fun trackViewCart(cartItems: List<Cart>) {
        val totalItems = cartItems.size
        val productIds = cartItems.joinToString(",") { it.productId }.take(40)
        val productNames = cartItems.joinToString(",") { it.productName }.take(40)

        val params = mapOf(
            "total_cart_items" to totalItems,
            "cart_item_ids" to productIds,
            "cart_item_names" to productNames
        )

        analyticsManager.logEvent(FirebaseAnalytics.Event.VIEW_CART, params)

        Log.d("CartAnalytics", "VIEW_CART - Items: ${cartItems.size}")
    }

    fun trackViewCartFailed(errorMessage: String) {
        analyticsManager.logEvent("view_cart_failed", mapOf(
            "error_message" to errorMessage
        ))
        Log.e("CartAnalytics", "VIEW_CART_FAILED - Error: $errorMessage")
    }

    fun trackCartQuantityChanged(productId: String, productName: String, variant: String, newQuantity: Int, action: String) {
        when (action) {
            "increase" -> analyticsManager.logEvent(FirebaseAnalytics.Event.ADD_TO_CART, mapOf(
                FirebaseAnalytics.Param.ITEM_ID to productId,
                FirebaseAnalytics.Param.ITEM_VARIANT to variant,
                FirebaseAnalytics.Param.ITEM_NAME to productName,
                FirebaseAnalytics.Param.QUANTITY to newQuantity.toLong()
            ))
            "decrease" -> analyticsManager.logEvent(FirebaseAnalytics.Event.REMOVE_FROM_CART, mapOf(
                FirebaseAnalytics.Param.ITEM_ID to productId,
                FirebaseAnalytics.Param.ITEM_VARIANT to variant,
                FirebaseAnalytics.Param.ITEM_NAME to productName,
                FirebaseAnalytics.Param.QUANTITY to newQuantity.toLong()
            ))
        }

        Log.d("CartAnalytics", "Cart quantity $action - $productName ($productId), Qty: $newQuantity")
    }

    fun trackBuyButtonClicked(totalPrice: Int) {
        analyticsManager.logEvent("buy_button_clicked", mapOf(
            FirebaseAnalytics.Param.PRICE to totalPrice
        ))
        Log.d("CartAnalytics", "BUY_BUTTON_CLICKED - Total: $totalPrice")
    }

    fun trackCheckedCartItems(checkedCarts: List<Cart>) {
        val totalItems = checkedCarts.size
        val productIds = checkedCarts.joinToString(",") { it.productId }.take(40)
        val productNames = checkedCarts.joinToString(",") { it.productName }.take(40)

        val params = mapOf(
            "total_checked_items" to totalItems,
            "checked_item_ids" to productIds,
            "checked_item_names" to productNames
        )

        analyticsManager.logEvent("cart_items_checked", params)
        Log.d("CartAnalytics", "Checked Cart Items: $params")
    }


    fun trackDetailCartItemSelected(product: ProductDetail, variant: ProductVariant) {
        val bundle = Bundle().apply {
            putString(FirebaseAnalytics.Param.ITEM_ID, product.productId )
            putString(FirebaseAnalytics.Param.ITEM_NAME, product.productName )
            putDouble(FirebaseAnalytics.Param.PRICE, product.productPrice?.toDouble() ?: 0.0)
            putString(FirebaseAnalytics.Param.ITEM_BRAND, product.brand)
        }
        analyticsManager.logEvent("cart_item_selected_from_detail", mapOf(
            FirebaseAnalytics.Param.ITEMS to bundle,
            "variant_name" to variant.variantName,
            "price" to (product.productPrice?.toDouble() ?: 0.0)
        ))
        Log.d("CartAnalytics", "Selected from detail - ${product.productName}, Variant: ${variant.variantName}")
    }



}