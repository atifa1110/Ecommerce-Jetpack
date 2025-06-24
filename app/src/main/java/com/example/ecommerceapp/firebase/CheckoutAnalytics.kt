package com.example.ecommerceapp.firebase

import android.util.Log
import com.example.core.ui.model.Fulfillment
import com.example.core.ui.model.Payment
import com.google.firebase.analytics.FirebaseAnalytics
import javax.inject.Inject

class CheckoutAnalytics @Inject constructor(
    private val analyticsManager: AnalyticsManager
) {

    fun trackPayButtonClicked() {
        analyticsManager.logButtonClick("pay_button")
        Log.d("CheckoutAnalytics", "Logged button_click: Payment_Button")
    }

    fun trackChoosePaymentButtonClicked() {
        analyticsManager.logButtonClick("choose_payment_button")
        Log.d("CheckoutAnalytics", "Logged button_click: Payment_item_Button")
    }

    fun trackChoosePaymentItem(paymentItem: Payment.PaymentItem) {
        analyticsManager.logEvent("choose_payment_selected", mapOf(
            "payment_item_title" to paymentItem.label,
            "payment_item_method" to "Image"
        ))
        Log.d("CheckoutAnalytics", "Logged payment_item_selected: ${paymentItem.label} - ${paymentItem.image}")
    }

    fun trackFulfillmentTransaction(fulfillment: Fulfillment) {
        analyticsManager.logEvent(FirebaseAnalytics.Event.PURCHASE, mapOf(
            FirebaseAnalytics.Param.TRANSACTION_ID to fulfillment.invoiceId,
            FirebaseAnalytics.Param.VALUE to fulfillment.total.toDouble(),
            FirebaseAnalytics.Param.PAYMENT_TYPE to fulfillment.payment,
            "fulfillment_date" to fulfillment.date,
            "fulfillment_time" to fulfillment.time,
            "fulfillment_status" to if (fulfillment.status) "success" else "failed"
        ))
        Log.d("CheckoutAnalytics", "FULFILLMENT - Invoice: ${fulfillment.invoiceId}, Status: ${fulfillment.status}")
    }

    fun trackFulfillmentTransactionFailed(errorMessage: String) {
        analyticsManager.logEvent("fulfillment_failed", mapOf(
            "error_message" to errorMessage
        ))
        Log.e("CheckoutAnalytics", "FULFILLMENT_FAILED - Error: $errorMessage")
    }
}