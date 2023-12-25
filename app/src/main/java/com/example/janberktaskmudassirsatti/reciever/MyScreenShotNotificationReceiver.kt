package com.example.janberktaskmudassirsatti.reciever

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.janberktaskmudassirsatti.service.MyScreenShootServiceJanBerk
import com.example.janberktaskmudassirsatti.ui.activities.ScreenShotActivity
import com.example.janberktaskmudassirsatti.utill.AppConstants.ACTION_EXIT
import com.example.janberktaskmudassirsatti.utill.AppConstants.ACTION_SCREENSHOT


class MyScreenShotNotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            ACTION_SCREENSHOT -> {
                val dialogIntent = Intent(context, ScreenShotActivity::class.java)
                dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context?.startActivity(dialogIntent)
            }

            ACTION_EXIT -> {
                val dialogIntent = Intent(context, MyScreenShootServiceJanBerk::class.java)
                context?.stopService(dialogIntent)
//                dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                context?.startActivity(dialogIntent)
//
//                val stopServiceIntent = Intent(ACTION_STOP_FOREGROUND)
//                context?.sendBroadcast(stopServiceIntent)
//                Toast.makeText(context, "Exit Action", Toast.LENGTH_SHORT).show()
            }
        }
    }
}