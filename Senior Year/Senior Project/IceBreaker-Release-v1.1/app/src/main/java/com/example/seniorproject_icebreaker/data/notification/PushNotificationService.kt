package com.example.seniorproject_icebreaker.data.notification

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.seniorproject_icebreaker.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class PushNotificationService : FirebaseMessagingService() {
    /*
    override fun onMessageReceived(message: RemoteMessage) {
        // Check if message contains a data payload.
        if (message.data.isNotEmpty()) {
            // Process any data
        }

        // Check if message contains a notification payload.
        message.notification?.let {
            val messageBody = it.body
            val messageTitle = it.title
            val builder = NotificationCompat.Builder(this, "Daily_Reminder_Channel_ID")
                .setSmallIcon(R.drawable.baseline_notifications_24)
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            // Check for POST_NOTIFICATIONS permission and request it if needed
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Notify MainActivity to request the permission
                val intent = Intent("REQUEST_NOTIFICATION_PERMISSION")
                sendBroadcast(intent)
                return
            }

            // Show the notification
            NotificationManagerCompat.from(this).notify(1, builder.build())
        }
    }
     */
}