package com.example.janberktaskmudassirsatti.reciever

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.projection.MediaProjectionManager
import android.widget.Toast
import com.example.janberktaskmudassirsatti.Appconstants.AppConstants.ACTION_EXIT
import com.example.janberktaskmudassirsatti.Appconstants.AppConstants.ACTION_SCREENSHOT
import com.example.janberktaskmudassirsatti.ScreenShotActivity


class MyReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            ACTION_SCREENSHOT -> {
                val dialogIntent = Intent(context, ScreenShotActivity::class.java)
                dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context?.startActivity(dialogIntent)

                //   Toast.makeText(context, "Screenshot Action", Toast.LENGTH_SHORT).show()
            }

            ACTION_EXIT -> {
                Toast.makeText(context, "Exit Action", Toast.LENGTH_SHORT).show()
            }
        }
    }

}