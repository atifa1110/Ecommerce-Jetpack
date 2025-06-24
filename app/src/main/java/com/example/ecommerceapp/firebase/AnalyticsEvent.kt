package com.example.ecommerceapp.firebase

import com.google.firebase.analytics.FirebaseAnalytics

object AnalyticsEvent {
    // Standard Firebase events
    const val LOGIN = FirebaseAnalytics.Event.LOGIN
    const val SIGN_UP = FirebaseAnalytics.Event.SIGN_UP
    const val VIEW_SEARCH_RESULTS = FirebaseAnalytics.Event.VIEW_SEARCH_RESULTS
    const val SELECT_ITEM = FirebaseAnalytics.Event.SELECT_ITEM
    const val VIEW_ITEM_LIST = FirebaseAnalytics.Event.VIEW_ITEM_LIST
    const val VIEW_ITEM = FirebaseAnalytics.Event.VIEW_ITEM
    const val ADD_TO_CART = FirebaseAnalytics.Event.ADD_TO_CART
    const val REMOVE_FROM_CART = FirebaseAnalytics.Event.REMOVE_FROM_CART
    const val VIEW_CART = FirebaseAnalytics.Event.VIEW_CART
    const val ADD_TO_WISHLIST = FirebaseAnalytics.Event.ADD_TO_WISHLIST
    const val BEGIN_CHECKOUT = FirebaseAnalytics.Event.BEGIN_CHECKOUT
    const val ADD_PAYMENT_INFO = FirebaseAnalytics.Event.ADD_PAYMENT_INFO
    const val PURCHASE = FirebaseAnalytics.Event.PURCHASE

    // Custom event
    const val BUTTON_CLICK = "button_click"

    object Param {
        const val ITEM_ID = FirebaseAnalytics.Param.ITEM_ID
        const val ITEM_NAME = FirebaseAnalytics.Param.ITEM_NAME
        const val ITEM_CATEGORY = FirebaseAnalytics.Param.ITEM_CATEGORY
        const val ITEM_LIST_NAME = FirebaseAnalytics.Param.ITEM_LIST_NAME
        const val SEARCH_TERM = FirebaseAnalytics.Param.SEARCH_TERM
        const val TRANSACTION_ID = FirebaseAnalytics.Param.TRANSACTION_ID
        const val VALUE = FirebaseAnalytics.Param.VALUE
        const val PAYMENT_TYPE = "payment_type"
        const val BUTTON_NAME = "button_name"
    }
}
