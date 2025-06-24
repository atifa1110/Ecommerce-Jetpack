package com.example.ecommerceapp.firebase

// AnalyticsManager.kt (Interface)
interface AnalyticsManager {
    /**
     * Mencatat sebuah event analitik generik dengan nama event dan parameter opsional.
     * Ini adalah metode dasar yang akan dipanggil oleh kelas helper analitik spesifik fitur.
     * @param eventName Nama event yang akan dicatat (misal: "button_click", "screen_view").
     * @param params Map opsional dari parameter event.
     */
    fun logEvent(eventName: String, params: Map<String, Any>? = null)

    /**
     * Mengatur properti pengguna. Properti ini akan dikaitkan dengan semua event di masa mendatang dari pengguna tersebut.
     * @param propertyName Nama properti (misal: "user_type", "subscription_status").
     * @param value Nilai properti (misal: "premium", "free").
     */
    fun setUserProperty(propertyName: String, value: String)

    /**
     * Mengatur ID pengguna. Sangat penting untuk mengidentifikasi pengguna unik di analitik.
     * @param userId ID unik pengguna.
     */
    fun setUserId(userId: String)

    /**
     * Menghapus semua data pengguna (misal: saat logout).
     */
    fun clearUserData()

    // Jika Anda punya event yang SANGAT umum dan sering digunakan di banyak fitur,
    // seperti klik tombol generik, Anda bisa meletakkannya di sini atau di kelas helper.
    // Contohnya `logButtonClick` yang sudah kita bahas sebelumnya bisa tetap di sini
    // atau dipindahkan ke helper jika dirasa lebih pas di sana.
    // Untuk tujuan abstraksi paling umum, sebaiknya tetap di sini.
    fun logButtonClick(buttonName: String) {
        logEvent("button_click", mapOf("button_name" to buttonName))
    }
}