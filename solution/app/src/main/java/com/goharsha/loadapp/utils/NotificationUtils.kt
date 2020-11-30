package com.goharsha.loadapp.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.annotation.IdRes
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.goharsha.loadapp.LoadApp
import com.goharsha.loadapp.R

@RequiresApi(Build.VERSION_CODES.O)
internal fun LoadApp.createNotificationChannels() {
    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    val channelId = resources.getResourceName(R.id.channel_download_completed)
    val channelName = getString(R.string.channel_download_completed_name)
    val importance = NotificationManager.IMPORTANCE_DEFAULT

    NotificationChannel(channelId, channelName, importance).apply {
        enableLights(true)
        lightColor = Color.RED
        enableVibration(true)

        notificationManager.createNotificationChannel(this)
    }
}

fun Context.showNotification(
    @IdRes channelId: Int,
    notificationId: Int,
    title: String,
    message: String,
    actionTitle: String? = null,
    onActionClick: PendingIntent? = null
) {
    with(NotificationCompat.Builder(this, resources.getResourceName(channelId))) {
        setContentTitle(title)
        setContentText(message)
        setSmallIcon(R.drawable.ic_assistant_black_24dp)
        setAutoCancel(true)

        actionTitle?.let {
            addAction(NotificationCompat.Action(null, actionTitle, onActionClick))
        }

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notificationId, build())
    }
}

fun Context.cancelNotification(notificationId: Int) {
    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.cancel(notificationId)
}