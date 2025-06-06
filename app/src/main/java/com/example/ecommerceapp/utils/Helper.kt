package com.example.ecommerceapp.utils

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import java.io.File
import java.text.NumberFormat
import java.util.Locale

fun uriToFile(context: Context, uri: Uri): File? {
    val inputStream = context.contentResolver.openInputStream(uri) ?: return null
    val tempFile = File.createTempFile("upload", ".jpg", context.cacheDir)
    tempFile.outputStream().use { outputStream ->
        inputStream.copyTo(outputStream)
    }
    return tempFile
}

@Composable
fun rememberImageBitmapFromUri(uri: Uri?): ImageBitmap? {
    val context = LocalContext.current
    return remember(uri) {
        uri?.let {
            try {
                context.contentResolver.openInputStream(uri)?.use { input ->
                    BitmapFactory.decodeStream(input)?.asImageBitmap()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}

fun currency(price: Int): String {
    val localId = Locale("in", "ID")
    val currencyFormat = NumberFormat.getCurrencyInstance(localId)
    return currencyFormat.format(price)
}