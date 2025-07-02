# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# --- Jetpack Compose ---
# Keep Composables (hindari error saat render atau recompile)
-keep class androidx.compose.** { *; }
-dontwarn androidx.compose.**

# Keep ViewModel state classes
-keepclassmembers class * {
    @androidx.compose.runtime.Composable <methods>;
}

# --- Retrofit + Gson / Moshi ---
# Keep Retrofit interfaces
-keep interface retrofit2.** { *; }
-dontwarn retrofit2.**

# Keep models for Gson
-keep class com.example.ecommerceapp.model.** { *; }
-keep class com.google.gson.** { *; }
-dontwarn com.google.gson.**

# If using Moshi instead of Gson
-keep class com.squareup.moshi.** { *; }
-dontwarn com.squareup.moshi.**

# --- Room (Database) ---
-keep class androidx.room.** { *; }
-dontwarn androidx.room.**
-keepclassmembers class * {
    @androidx.room.** <methods>;
}

# --- Hilt / Dagger ---
-keep class dagger.** { *; }
-dontwarn dagger.**
-keep class javax.inject.** { *; }
-dontwarn javax.inject.**
-keep class * extends dagger.hilt.android.internal.lifecycle.HiltViewModelFactory { *; }

# Keep generated Hilt components
-keep class * extends dagger.hilt.internal.GeneratedComponent { *; }

# --- Kotlin Coroutines ---
-dontwarn kotlinx.coroutines.**

# --- ViewModel + State ---
-keepclassmembers class androidx.lifecycle.ViewModel {
    <init>();
}

# --- Coil (Image Loading) ---
-keep class coil.** { *; }
-dontwarn coil.**

# --- Firebase SDK ---
-keep class com.google.firebase.** { *; }
-dontwarn com.google.firebase.**

# --- Misc ---
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.example.ecommerceapp.** { *; }

# Optional: Disable obfuscation for easier crashlytics logs (recommended for early prod)
-dontobfuscate
