package com.example.janberktaskmudassirsatti.di

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.janberktaskmudassirsatti.R
import com.example.janberktaskmudassirsatti.di.repositories.NotificationRepository
import com.example.janberktaskmudassirsatti.reciever.MyScreenShotNotificationReceiver
import com.example.janberktaskmudassirsatti.ui.activities.MainActivity
import com.example.janberktaskmudassirsatti.utill.AppConstants.ACTION_EXIT
import com.example.janberktaskmudassirsatti.utill.AppConstants.ACTION_SCREENSHOT
import com.example.janberktaskmudassirsatti.utill.AppConstants.CHANNEL_ID
import com.example.janberktaskmudassirsatti.utill.AppConstants.CHANNEL_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NotificationModule {

    @Singleton
    @Provides
    fun provideNotificationManager(
        @ApplicationContext context: Context
    ): NotificationManagerCompat {
        val notificationManager = NotificationManagerCompat.from(context)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
        return notificationManager
    }


    @Singleton
    @Provides
    fun provideNotificationBuilder(
        @ApplicationContext context: Context
    ): NotificationCompat.Builder {
        val notificationLayout =
            RemoteViews(context.packageName, R.layout.custom_notification_layout_small)
        val mainActivityIntent = Intent(context, MainActivity::class.java)
        mainActivityIntent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP

        val pendingDefaultIntent = PendingIntent.getActivity(
            context, 0,
            mainActivityIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        notificationLayout.setOnClickPendingIntent(
            R.id.main_layout,
            pendingDefaultIntent
        )

        val screenshotIntent = Intent(context, MyScreenShotNotificationReceiver::class.java)
        screenshotIntent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        screenshotIntent.action = ACTION_SCREENSHOT
        val pendingScreenshotIntent = PendingIntent.getBroadcast(
            context, 0,
            screenshotIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val exitIntent = Intent(context, MyScreenShotNotificationReceiver::class.java)
        exitIntent.action = ACTION_EXIT
        val pendingExitIntent = PendingIntent.getBroadcast(
            context, 0,
            exitIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        notificationLayout.setOnClickPendingIntent(
            R.id.btn_screenshot,
            pendingScreenshotIntent
        )

        notificationLayout.setOnClickPendingIntent(
            R.id.btn_exit,
            pendingExitIntent
        )
        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setCustomContentView(notificationLayout)
            .setSmallIcon(R.drawable.ic_stat_name)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
    }

    /** Notification repository provider*/
    @Singleton
    @Provides
    fun provideNotificationRepository(
        notificationBuilder: NotificationCompat.Builder,
        notificationManager: NotificationManagerCompat,
        @ApplicationContext context: Context
    ): NotificationRepository {
        return NotificationRepository(notificationBuilder, context)
    }

}

