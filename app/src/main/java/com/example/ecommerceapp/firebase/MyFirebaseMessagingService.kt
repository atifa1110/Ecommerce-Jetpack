package com.example.ecommerceapp.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.ecommerceapp.MainActivity
import com.example.ecommerceapp.R
import com.example.core.domain.model.NotificationModel
import com.example.ecommerceapp.graph.NotificationDestination
import com.example.core.domain.usecase.AddNotificationUseCase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject

@AndroidEntryPoint
class MyFirebaseMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var addNotificationUseCase: AddNotificationUseCase

    private val TAG = "FirebaseMessagingService"

    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
        // You can send this token to your server if needed.
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val data = remoteMessage.data

        val title = data["title"].orEmpty()
        val body = data["body"].orEmpty()
        val image = data["image"].orEmpty()
        val type = data["type"].orEmpty()
        val date = data["date"].orEmpty()
        val time = data["time"].orEmpty()

        sendNotification(title, body, image, type, date, time)

        Log.d(TAG, "Message data payload: $data")
        remoteMessage.notification?.let {
            Log.d(TAG, "Message Notification Body: ${it.body}")
        }
    }

    private fun getBitmapFromURL(imageUrl: String): Bitmap? {
        return try {
            val url = URL(imageUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            BitmapFactory.decodeStream(connection.inputStream)
        } catch (e: IOException) {
            Log.e(TAG, "Image loading error: ${e.localizedMessage}")
            null
        }
    }

    private fun sendNotification(
        title: String,
        body: String,
        image: String,
        type: String,
        date: String,
        time: String
    ) {
        // Save notification to local database using use case
        CoroutineScope(Dispatchers.IO).launch {
            try {
                addNotificationUseCase(
                    NotificationModel(
                        title = title,
                        body = body,
                        image = image,
                        type = type,
                        date = date,
                        time = time,
                        isRead = false
                    )
                )
            } catch (e: Exception) {
                Log.e(TAG, "Failed to add notification: ${e.message}")
            }
        }

        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("navigate_to", NotificationDestination.route)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TOP)
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            FLAG_IMMUTABLE
        )

        val channelId = getString(R.string.default_notification_channel_id)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val bitmap = getBitmapFromURL(image)
        bitmap?.let {
            notificationBuilder.setLargeIcon(it)
        }

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "General Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
    }
}
