package com.example.ecommerceapp.firebase

import android.util.Log
import javax.inject.Inject

class StatusAnalytics @Inject constructor(
    private val analyticsManager: AnalyticsManager
) {
    fun trackReviewButtonClicked() {
        analyticsManager.logButtonClick("review_button")
        Log.d("StatusAnalytics", "Logged button_click: Review_Button")
    }

    fun trackDoneButtonClicked() {
        analyticsManager.logButtonClick("done_button")
        Log.d("StatusAnalytics", "Logged button_click: Status_Button")
    }

    fun trackStatusTransaction(message : String){
        analyticsManager.logEvent("transaction_success", mapOf(
            "success_message" to message
        ))
        Log.e("StatusAnalytics", "TRANSACTION_FAILED - Error: $message")
    }

    fun trackStatusTransactionFailed(errorMessage: String) {
        analyticsManager.logEvent("transaction_failed", mapOf(
            "error_message" to errorMessage
        ))
        Log.e("StatusAnalytics", "TRANSACTION_FAILED - Error: $errorMessage")
    }
}