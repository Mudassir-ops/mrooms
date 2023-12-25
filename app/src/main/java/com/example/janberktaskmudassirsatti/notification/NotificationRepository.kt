package com.example.janberktaskmudassirsatti.notification

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import javax.inject.Inject

class NotificationRepository @Inject constructor(
    private val notificationBuilder: NotificationCompat.Builder,
    private val notificationManager: NotificationManagerCompat,
    private val context: Context
) {


    fun sendNotification(): NotificationCompat.Builder? {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return null
        }
        notificationManager.notify(1, notificationBuilder.build())

        return notificationBuilder
    }

    fun buildNotification(): NotificationCompat.Builder? {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return null
        }

        return notificationBuilder
    }


    fun updateNotificationContent(title: String, content: String) {
        notificationBuilder.setContentTitle(title).setContentText(content)
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        notificationManager.notify(1, notificationBuilder.build())
    }

}