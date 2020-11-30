package com.goharsha.loadapp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.O)
internal fun LoadApp.createNotificationChannels() {
    val notificationManager = getSystemService(Application.NOTIFICATION_SERVICE) as NotificationManager

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
