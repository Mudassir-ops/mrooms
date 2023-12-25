package com.example.janberktaskmudassirsatti.di.repositories

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import javax.inject.Inject

class NotificationRepository @Inject constructor(
    private val notificationBuilder: NotificationCompat.Builder,
    private val context: Context
) {
    fun buildNotification(): NotificationCompat.Builder {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    context as Activity,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    99
                )
            }
        }
        return notificationBuilder
    }
}