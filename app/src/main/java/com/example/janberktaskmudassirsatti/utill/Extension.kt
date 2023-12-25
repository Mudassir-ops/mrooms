package com.example.janberktaskmudassirsatti.utill

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.graphics.Bitmap
import android.hardware.display.DisplayManager
import android.media.projection.MediaProjection
import android.util.Log
import android.view.View
import androidx.appcompat.widget.PopupMenu
import com.example.janberktaskmudassirsatti.R
import com.example.janberktaskmudassirsatti.utill.AppConstants.TAG
import java.io.ByteArrayOutputStream


fun getVirtualDisplayFlags(): Int {
    return DisplayManager.VIRTUAL_DISPLAY_FLAG_OWN_CONTENT_ONLY or DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC
}

/** Extension to convert bitmap tp compress bytearray*/
fun Bitmap?.compressBitmapToByteArray(): ByteArray {
    val outputStream = ByteArrayOutputStream()
    this?.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
    return outputStream.toByteArray()
}

/** Extension to stop media projection and close current activity*/
fun Activity.stopCapturingImages(mediaProjection: MediaProjection?) {
    mediaProjection?.stop()
    finish()
}

fun String.printLog() {
    Log.d(TAG, this)
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun Context.finishPreviousActivity() {
    val am = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val tasks = am.appTasks
    if (tasks.isNotEmpty()) {
        tasks[0].finishAndRemoveTask()
    }
}

fun View?.showPopupMenu(callback: (ActionType) -> Unit) {

    //val popupMenu = this?.let { this.context?.let { it1 -> PopupMenu(it1, it, 0, 0, R.style.PopupMenuStyle) } }

    val popupMenu = this@showPopupMenu?.let { context?.let { it1 -> PopupMenu(it1, it) } }
    popupMenu?.menuInflater?.inflate(R.menu.cutom_action_menu, popupMenu.menu)
    popupMenu?.setOnMenuItemClickListener { menuItem ->
        when (menuItem.itemId) {
            R.id.menu_open -> {
                callback.invoke(ActionType.ACTION_OPEN)
                true
            }

            R.id.menu_share -> {
                callback.invoke(ActionType.ACTION_SHARE)
                true
            }

            R.id.menu_delete -> {
                callback.invoke(ActionType.ACTION_DELETE)
                true
            }

            else -> false
        }
    }
    popupMenu?.show()
}
