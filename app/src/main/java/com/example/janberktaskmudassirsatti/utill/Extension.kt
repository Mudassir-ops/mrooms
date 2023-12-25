package com.example.janberktaskmudassirsatti.utill

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.app.RecoverableSecurityException
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.hardware.display.DisplayManager
import android.media.projection.MediaProjection
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.FileProvider
import com.example.janberktaskmudassirsatti.R
import com.example.janberktaskmudassirsatti.utill.AppConstants.TAG
import java.io.ByteArrayOutputStream
import java.io.File


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
    this.finishAffinity()
    this.finish()
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


fun Activity.permissionDialog(
    yesButtonText: String,
    noButtonText: String,
    messageText: String,
    isCancelable: Boolean = true,
    onYesButtonClick: () -> Unit,
    onNoButtonClick: () -> Unit
) {
    val dialogClickListener: DialogInterface.OnClickListener =
        DialogInterface.OnClickListener { dialog, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
                    dialog.dismiss()
                    onYesButtonClick()
                }

                DialogInterface.BUTTON_NEGATIVE -> {
                    dialog.dismiss()
                    onNoButtonClick()
                }
            }
        }

    val builder: AlertDialog.Builder = AlertDialog.Builder(this)
    builder.setMessage(messageText).setPositiveButton(yesButtonText, dialogClickListener)
        .setNegativeButton(noButtonText, dialogClickListener).setCancelable(isCancelable).show()
}


fun isServiceRunning(context: Context, serviceClass: Class<*>): Boolean {
    val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager?
    val runningServices = activityManager?.getRunningServices(Integer.MAX_VALUE)

    if (runningServices != null) {
        for (service in runningServices) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
    }
    return false
}


fun Context.shareImage(imagePath: String, shareTitle: String = "Share Image") {
    val imageFile = File(imagePath)
    if (!imageFile.exists()) {
        return
    }
    val imageUri = FileProvider.getUriForFile(
        this, "${this.packageName}.provider", imageFile
    )
    val shareIntent = Intent(Intent.ACTION_SEND)
    shareIntent.type = "image/*"
    shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri)
    shareIntent.putExtra(Intent.EXTRA_SUBJECT, shareTitle)
    shareIntent.putExtra(Intent.EXTRA_TEXT, "Enjoy Your Image")
    val chooserIntent = Intent.createChooser(shareIntent, shareTitle)
    chooserIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION,)
    startActivity(chooserIntent)
}


@SuppressLint("Recycle")
fun Context?.getImgUri(
    path: String,
): Uri? {
    try {
        val checkFile = File(path)
        "checkDelete- $checkFile".printLog()
        if (checkFile.exists()) {
            var id: Long = 0
            val cr: ContentResolver = this?.contentResolver ?: return null
            val selection = MediaStore.Images.Media.DATA
            val selectionArgs = arrayOf<String>(checkFile.absolutePath)
            val projection = arrayOf(MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA)
            val cursor = cr.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
                "$selection=?", selectionArgs, null
            )

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    val idIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                    id = cursor.getString(idIndex).toLong()
                    id.toString().printLog()
                    try {
                        return ContentUris.withAppendedId(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            id
                        )
                    } catch (securityException: SecurityException) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            val recoverableSecurityException =
                                securityException as? RecoverableSecurityException
                                    ?: throw securityException
                            recoverableSecurityException.userAction.actionIntent.intentSender
                        } else {
                            throw securityException
                        }
                    }

                }
            }
        }
    } catch (ex: Exception) {
        ex.printStackTrace()
    }
    return null
}