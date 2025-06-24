package com.example.ecommerceapp.firebase

import javax.inject.Inject
import javax.inject.Singleton
import android.util.Log

@Singleton
class AuthAnalytics @Inject constructor(
    private val analyticsManager: AnalyticsManager
) {

    fun trackLoginAttempt(email: String, isPasswordProvided: Boolean, hasPasswordError: Boolean) {
        analyticsManager.logEvent("login_attempt", mapOf(
            "login_method" to "email_password", // Parameter untuk mengidentifikasi metode login
            "email_status" to if (email.isNotBlank()) "provided" else "empty", // Status input email
            "password_status" to if (isPasswordProvided) "provided" else "empty", // Status input password
            "password_error_status" to if (hasPasswordError) "has_error" else "no_error", // Status error validasi password
            "email_domain" to email.substringAfter("@", "unknown") // Domain email untuk analisis demografi
        ))
        Log.d("AuthAnalytics", "Logged login_attempt for email: $email, pass provided: $isPasswordProvided, pass error: $hasPasswordError")
    }

    fun trackLoginSuccess(username: String, email: String) {
        analyticsManager.logEvent("login_success", mapOf(
            "user_name" to username,
            "login_method" to "email_password",
            "email_domain" to email.substringAfter("@", "unknown")
        ))
        // Setelah login sukses, atur user ID di AnalyticsManager untuk semua event berikutnya
        analyticsManager.setUserProperty("Username",username)
        Log.d("AuthAnalytics", "Logged login_success for user: $username")
    }

    fun trackLoginFailure(errorMessage: String?, email: String) {
        analyticsManager.logEvent("login_failure", mapOf(
            "login_method" to "email_password",
            "error_message" to (errorMessage ?: "unknown_error"), // Pesan error kegagalan
            "email_domain" to email.substringAfter("@", "unknown")
        ))
        Log.d("AuthAnalytics", "Logged login_failure: $errorMessage for email: $email")
    }

    fun trackSignUpAttempt(email: String, isPasswordProvided: Boolean, hasPasswordError: Boolean) {
        analyticsManager.logEvent("sign_up_attempt", mapOf(
            "sign_up_method" to "email_password", // Parameter untuk mengidentifikasi metode login
            "email_status" to if (email.isNotBlank()) "provided" else "empty", // Status input email
            "password_status" to if (isPasswordProvided) "provided" else "empty", // Status input password
            "password_error_status" to if (hasPasswordError) "has_error" else "no_error", // Status error validasi password
            "email_domain" to email.substringAfter("@", "unknown") // Domain email untuk analisis demografi
        ))
        Log.d("AuthAnalytics", "Logged sign_up_attempt")
    }

    fun trackSignUpSuccess(token: Boolean) {
        analyticsManager.logEvent("sign_up_success", mapOf(
            "user_token" to token
        ))
        analyticsManager.setUserProperty("Token", token.toString()) // Set user ID setelah pendaftaran
        Log.d("AuthAnalytics", "Logged sign_up_success for token: $token")
    }

    fun trackSignUpFailure(errorMessage: String?,email: String) {
        analyticsManager.logEvent("sign_up_failure", mapOf(
            "sign_up_method" to "email_password",
            "error_message" to (errorMessage ?: "unknown_error"), // Pesan error kegagalan
            "email_domain" to email.substringAfter("@", "unknown")
        ))
        Log.d("AuthAnalytics", "Logged sign_up_failure: $errorMessage")
    }

    fun trackLoginButtonClicked() {
        analyticsManager.logButtonClick("Login_Button")
        Log.d("AuthAnalytics", "Logged button_click: Login_Button")
    }

    fun trackSignUpButtonClicked() {
        analyticsManager.logButtonClick("Sign_Up_Button")
        Log.d("AuthAnalytics", "Logged button_click: Sign_Up_Button")
    }

    fun trackLogoutButtonClicked() {
        analyticsManager.logButtonClick("Logout_Button")
        Log.d("AuthAnalytics", "Logged button_click: Logout_Button")
    }

}