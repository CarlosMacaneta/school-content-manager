package com.cs.schoolcontentmanager.data.service

import android.app.NotificationManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.cs.schoolcontentmanager.utils.Constants
import com.cs.schoolcontentmanager.utils.Constants.NOTIFICATION_ID
import com.cs.schoolcontentmanager.utils.Util.createNotificationChannel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import timber.log.Timber

class PushNotificationService() : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        val instance = FirebaseFirestore.getInstance()
        Timber.e("n", instance.app.name)
        instance.collection("DeviceTokens").document().set(mutableMapOf("token" to token))
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val notification = message.notification

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            createNotificationChannel(this, NotificationManager.IMPORTANCE_HIGH)

        Timber.e("n", notification?.title)

        val builder = NotificationCompat.Builder(
            applicationContext,
            Constants.CHANNEL_ID
        ).apply {
            notification?.let {
                setContentTitle(it.title)
                setContentText(it.body)
            }
            setAutoCancel(true)
        }
        NotificationManagerCompat.from(this).apply {
            notify(NOTIFICATION_ID, builder.build())
        }
    }
}