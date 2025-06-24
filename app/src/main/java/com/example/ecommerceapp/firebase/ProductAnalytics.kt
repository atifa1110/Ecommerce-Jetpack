package com.example.ecommerceapp.firebase

import android.util.Log
import com.google.firebase.analytics.FirebaseAnalytics

import com.example.core.ui.model.ProductDetail
import com.example.core.ui.model.ProductVariant
import javax.inject.Inject

class ProductAnalyticsManager @Inject constructor(
    private val analyticsManager: AnalyticsManager
) {

    fun trackViewSearchResults(searchTerm: String, displayedProductIds: List<String>) {
        val joinedProductIds = displayedProductIds.joinToString(",")

        val maxParamLength = 90
        val limitedProductIdsString = if (joinedProductIds.length > maxParamLength) {
            val truncated = joinedProductIds.substring(0, maxParamLength)
            Log.w("AnalyticsDebug", "Truncating displayed_product_ids from ${joinedProductIds.length} to ${truncated.length}. Value: $truncated...")
            "$truncated..."
        } else {
            Log.d("AnalyticsDebug", "displayed_product_ids length: ${joinedProductIds.length}. Value: $joinedProductIds")
            joinedProductIds
        }// Truncate if too long (e.g., Firebase's max is 256 chars)

        analyticsManager.logEvent(
            FirebaseAnalytics.Event.VIEW_SEARCH_RESULTS, mapOf(
                FirebaseAnalytics.Param.SEARCH_TERM to searchTerm,
                "displayed_product_ids" to limitedProductIdsString,
                FirebaseAnalytics.Param.ITEM_LIST_NAME to "Search Results"
            )
        )
        Log.d("ProductAnalytics", "Search Term: $searchTerm, Products: ${displayedProductIds.size}")
    }

    fun trackSearchSuggestionSelected(suggestion: String) {
        analyticsManager.logEvent("search_suggestion_selected", mapOf("suggestion" to suggestion))
        Log.d("ProductAnalytics", "SEARCH_SUGGESTION_SELECTED - Suggestion: $suggestion")
    }

    fun trackSearchScreenViewed() {
        analyticsManager.logEvent("search_screen_viewed")
        Log.d("ProductAnalytics","Analytics: SEARCH_SCREEN_VIEWED")
    }

    fun trackSearchScreenClosed() {
        analyticsManager.logEvent("search_screen_closed")
        Log.d("ProductAnalytics","Analytics: SEARCH_SCREEN_CLOSED")
    }

    fun trackSearchFailed(query: String, errorMessage: String) {
        analyticsManager.logEvent("search_failed", mapOf("search_query" to query, "error_message" to errorMessage))
        Log.d("ProductAnalytics","Analytics: SEARCH_FAILED - Query: $query, Error: $errorMessage")
    }

    fun trackSearchSuccess(query: String, resultCount: Int) {
        analyticsManager.logEvent("search_success", mapOf("search_query" to query, "result_count" to resultCount))
        Log.d("ProductAnalytics","Analytics: SEARCH_SUCCESS - Query: $query, Results: $resultCount")
    }


    fun trackApplyFilter(filterType: String, filterValue: String, productListContext: String) {
        analyticsManager.logEvent(
            "apply_filter", mapOf(
                "filter_type" to filterType,
                "filter_value" to filterValue,
                "product_list_context" to productListContext
            )
        )
        Log.d("ProductAnalytics","Apply Filter : filter_type: $filterType, Value: $filterValue, Context: $productListContext")
    }

    fun trackViewItemDetail(productDetail: ProductDetail) {
        val paramsMap = mutableMapOf<String, Any>()
        paramsMap[FirebaseAnalytics.Param.ITEM_ID] = productDetail.productId ?: "UNKNOWN_ID"
        paramsMap[FirebaseAnalytics.Param.ITEM_NAME] = productDetail.productName ?: "UNKNOWN_NAME"
        paramsMap[FirebaseAnalytics.Param.PRICE] = productDetail.productPrice?.toDouble() ?: 0.0
        paramsMap[FirebaseAnalytics.Param.ITEM_BRAND] = productDetail.brand ?: "UNKNOWN_BRAND"
        paramsMap[FirebaseAnalytics.Param.DISCOUNT] = productDetail.sale?.toDouble() ?: 0.0
        paramsMap["store_name"] = productDetail.store ?: "UNKNOWN_STORE"
        paramsMap["stock_quantity"] = productDetail.stock?.toLong() ?: 0L
        paramsMap["total_rating_count"] = productDetail.totalRating?.toLong() ?: 0L
        paramsMap["total_review_count"] = productDetail.totalReview?.toLong() ?: 0L
        paramsMap["total_satisfaction_score"] = productDetail.totalSatisfaction?.toLong() ?: 0L
        paramsMap["product_rating_value"] = productDetail.productRating?.toDouble() ?: 0.0
        paramsMap["is_wishlist"] = if (productDetail.isWishlist) 1L else 0L
        paramsMap["image_count"] = productDetail.image?.size?.toLong() ?: 0L
        paramsMap["variant_count"] = productDetail.productVariant?.size?.toLong() ?: 0L

        analyticsManager.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, paramsMap)
        Log.d("ProductAnalytics","Analytics: VIEW_ITEM - Product Detail: ${productDetail.productName}, ID: ${productDetail.productId}")
    }

    fun trackSelectedVariantDetail(productId: String, selectedVariant : ProductVariant) {
        analyticsManager.logEvent(FirebaseAnalytics.Event.SELECT_ITEM, mapOf(
            FirebaseAnalytics.Param.ITEM_ID to productId,
            "variant_name" to selectedVariant.variantName
        ))
        Log.d("ProductAnalytics","Selected Variant : Product Detail: ${productId}, VariantName : ${selectedVariant.variantName}")
    }

    fun trackDetailWishlistButtonClicked(productId: String, isNowWishlist: Boolean) {
        analyticsManager.logEvent("Wishlist_Button", mapOf(
            "product_id" to productId,
            "wishlist_action" to if (isNowWishlist) "add" else "remove"
        ))
    }

    fun trackDetailAddToCartButtonClicked() {
        analyticsManager.logButtonClick("Cart_Button")
        Log.d("ProductAnalytics", "Detail button_click: Cart_Button")
    }

    fun trackDetailAddToCheckoutButtonClicked() {
        analyticsManager.logButtonClick("Checkout_Button")
        Log.d("ProductAnalytics", "Detail button_click: Checkout_Button")
    }

    fun trackSearchButtonClicked() {
        analyticsManager.logButtonClick("Search_Button")
        Log.d("ProductAnalytics", "Filter button_click: Search_Button")
    }

    fun trackFilterResetButtonClicked() {
        analyticsManager.logButtonClick("Filter_Reset_Button")
        Log.d("ProductAnalytics","Filter Click: Filter_Reset_Button")
    }

    fun trackFilterButtonClicked() {
        analyticsManager.logButtonClick("Filter_Button")
        Log.d("ProductAnalytics", "Filter Click: Filter_Button")
    }
}