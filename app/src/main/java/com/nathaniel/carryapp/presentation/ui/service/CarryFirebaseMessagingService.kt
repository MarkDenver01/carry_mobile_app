package com.nathaniel.carryapp.presentation.ui.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.nathaniel.carryapp.R
import com.nathaniel.carryapp.data.local.room.entity.NotificationEntity
import com.nathaniel.carryapp.presentation.ui.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class CarryFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Timber.d("NEW FCM TOKEN (ANDROID): $token")

        val repository = ServiceLocator.apiRepository

        CoroutineScope(Dispatchers.IO).launch {
            try {
                // ✅ WALANG customerId SA FIRST LAUNCH
                repository.registerAndroidToken(token)
                Timber.d("✅ Token registered WITHOUT customerId")
            } catch (e: Exception) {
                Timber.e("❌ Token register error: ${e.message}")
            }
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val title = message.notification?.title ?: "Wrap & Carry"
        val body = message.notification?.body ?: "You have a new notification"

        // ✅ Custom data from backend (type, orderId)
        val type = message.data["type"]
        val orderId = message.data["orderId"]

        val notification = NotificationEntity(
            title = title,
            body = body,
            type = type,
            orderId = orderId
        )

        CoroutineScope(Dispatchers.IO).launch {
            ServiceLocator.localRepository.saveNotification(notification)
        }

        showNotification(title, body, type, orderId)
    }


    private fun showNotification(
        title: String,
        body: String,
        type: String?,
        orderId: String?
    ) {
        if (!NotificationManagerCompat.from(this).areNotificationsEnabled()) {
            Timber.w("❌ Notifications are BLOCKED by user")
            return
        }

        val channelId = "carry_order_notifications"

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("type", type)
            putExtra("orderId", orderId)
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        // ✅ Required for Android 8+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Order Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notif_icon) // palitan kung may custom icon ka
            .setColor(getColor(R.color.white))
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }
}
