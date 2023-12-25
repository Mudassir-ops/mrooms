package com.example.janberktaskmudassirsatti.di.repositories

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import javax.inject.Inject

class NotificationRepository @Inject constructor(
    private val notificationBuilder: NotificationCompat.Builder,
    private val context: Context
) {
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
}