package com.example.ecommerceapp.firebase

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import javax.inject.Inject
import javax.inject.Singleton
import android.util.Log // Untuk debugging

// FirebaseAnalyticsManager.kt
@Singleton
class FirebaseAnalyticsManager @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics
) : AnalyticsManager {

    override fun logEvent(eventName: String, params: Map<String, Any>?) {
        val bundle = Bundle().apply {
            params?.forEach { (key, value) ->
                when (value) {
                    is String -> putString(key, value)
                    is Int -> putInt(key, value)
                    is Long -> putLong(key, value)
                    is Double -> putDouble(key, value)
                    is Boolean -> putBoolean(key, value)
                    // Anda bisa tambahkan dukungan untuk Array/List jika event Anda butuh itu
                    // Pastikan Firebase Analytics mendukung tipe data ini di Bundle
                    else -> putString(key, value.toString()) // Fallback
                }
            }
        }
        firebaseAnalytics.logEvent(eventName, bundle)
        Log.d("AnalyticsManager", "Logged event: $eventName with params: $params")
    }

    override fun setUserProperty(propertyName: String, value: String) {
        firebaseAnalytics.setUserProperty(propertyName, value)
        Log.d("AnalyticsManager", "Set user property: $propertyName = $value")
    }

    override fun setUserId(userId: String) {
        firebaseAnalytics.setUserId(userId)
        Log.d("AnalyticsManager", "Set user ID: $userId")
    }

    override fun clearUserData() {
        firebaseAnalytics.setUserId(null)
        // Logika untuk menghapus user properties spesifik jika ada
        Log.d("AnalyticsManager", "Cleared user data")
    }
}