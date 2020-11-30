package com.goharsha.loadapp

import android.app.Application
import android.os.Build

class LoadApp : Application() {
    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannels()
        }
    }
}