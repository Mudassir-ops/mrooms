package com.example.janberktaskmudassirsatti.service;

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.example.janberktaskmudassirsatti.notification.NotificationRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MyScreenShootServiceJanBerk : Service() {

    private val TAG = "MY_SERVICE"

    @Inject
    lateinit var notificationRepository: NotificationRepository

    override fun onCreate() {
        super.onCreate()
        val notification = notificationRepository.buildNotification()
        startForeground(1, notification?.build())
    }

    override fun onBind(intent: Intent): IBinder? = null
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

}